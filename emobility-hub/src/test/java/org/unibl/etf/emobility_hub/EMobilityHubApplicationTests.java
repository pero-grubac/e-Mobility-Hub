package org.unibl.etf.emobility_hub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.unibl.etf.emobility_hub.security.jwt.JwtService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

// End-to-end against a real MySQL 8. Flyway creates the schema and loads the
// demo data, exactly as in docker compose. Skipped when Docker is not available.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class EMobilityHubApplicationTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0").withDatabaseName("emobility");

    @DynamicPropertySource
    static void uploads(DynamicPropertyRegistry registry) throws Exception {
        Path dir = Files.createTempDirectory("emobility-uploads");
        registry.add("file.upload-dir", dir::toString);
    }

    static final Map<String, String> PASSWORDS = Map.of(
            "admin", "admin123", "operator", "operator", "manager", "manager", "client1", "operator");

    @Autowired
    TestRestTemplate rest;
    @Autowired
    JwtService jwtService;
    @Autowired
    ObjectMapper json;
    @Autowired
    JdbcTemplate jdbc;

    final Map<String, String> tokens = new HashMap<>();

    ResponseEntity<String> loginResponse(String username, String password) {
        return rest.postForEntity("/auth/login", Map.of("username", username, "password", password), String.class);
    }

    String token(String username) {
        return tokens.computeIfAbsent(username, u -> {
            ResponseEntity<String> response = loginResponse(u, PASSWORDS.get(u));
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            return read(response.getBody()).get("token").asText();
        });
    }

    JsonNode read(String body) {
        try {
            return json.readTree(body);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    HttpEntity<?> as(String username, Object body) {
        HttpHeaders headers = new HttpHeaders();
        if (username != null) {
            headers.setBearerAuth(token(username));
        }
        return new HttpEntity<>(body, headers);
    }

    ResponseEntity<String> call(HttpMethod method, String url, String username) {
        return rest.exchange(url, method, as(username, null), String.class);
    }

    JsonNode get(String url, String username) {
        ResponseEntity<String> response = call(HttpMethod.GET, url, username);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return read(response.getBody());
    }

    // ── Authentication ──────────────────────────────────────────────────────

    @Test
    void seededAccountsCanLogIn() {
        assertThat(jwtService.extractUsername(token("admin"))).isEqualTo("admin");
        assertThat(jwtService.extractUsername(token("operator"))).isEqualTo("operator");
        assertThat(jwtService.extractUsername(token("manager"))).isEqualTo("manager");
    }

    @Test
    void wrongPasswordAndUnknownUserAreUnauthorized() {
        assertThat(loginResponse("admin", "nope").getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(loginResponse("nobody", "x").getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void loginSetsAnHttpOnlySessionCookieThatAuthenticatesTheBrowser() {
        ResponseEntity<String> login = loginResponse("manager", "manager");
        String setCookie = login.getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .filter(c -> c.startsWith("EMH_SESSION=")).findFirst().orElseThrow();
        assertThat(setCookie).startsWith("EMH_SESSION=").contains("HttpOnly", "SameSite=Strict", "Path=/");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, setCookie.substring(0, setCookie.indexOf(';')));
        ResponseEntity<String> me = rest.exchange("/auth/me", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(me.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(read(me.getBody()).get("role").asText()).isEqualTo("ROLE_MANAGER");
        assertThat(read(me.getBody()).get("expiresAt").asText()).isNotBlank();
    }

    // ── Role rules: who may call what ───────────────────────────────────────

    @ParameterizedTest(name = "{0} {1} as {2} -> {3}")
    @CsvSource({
            "GET,    /electric-cars,                   ,         401",
            "GET,    /electric-cars,                   operator, 200",
            "GET,    /electric-cars,                   client1,  403",
            "DELETE, /electric-cars/999,               operator, 403",
            "DELETE, /electric-cars/999,               admin,    404",
            "GET,    /manufacturers/getAll,            operator, 200",
            "DELETE, /manufacturers/999,               operator, 403",
            "GET,    /clients,                         operator, 200",
            "DELETE, /clients/999,                     operator, 403",
            "GET,    /users,                           operator, 403",
            "GET,    /users,                           admin,    200",
            "DELETE, /users/999,                       admin,    404",
            "GET,    /rentals,                         operator, 200",
            "DELETE, /faults/999,                      operator, 403",
            "GET,    /statistics/getRevenueByVehicleType, admin, 403",
            "GET,    /statistics/getRevenueByVehicleType, manager, 200",
            "GET,    /promotions,                      admin,    403",
            "GET,    /promotions,                      manager,  200",
            "GET,    /promotions/1,                    ,         200",
            "GET,    /rss/promotions,                  ,         200",
            "GET,    /actuator/health,                 ,         200",
            "GET,    /v3/api-docs,                     ,         200",
            "GET,    /vehicles,                        operator, 200",
            "GET,    /vehicles/locations,              operator, 200",
            "GET,    /vehicles,                        client1,  403",
            "GET,    /rss,                             ,         200",
            "GET,    /not-an-endpoint,                 admin,    403",
    })
    void roleRules(HttpMethod method, String url, String user, int expected) {
        assertThat(call(method, url, user).getStatusCode().value()).isEqualTo(expected);
    }

    @Test
    void employeesCannotBeCreatedAnonymously() {
        Map<String, String> admin = Map.of("username", "intruder", "password", "x",
                "firstName", "I", "lastName", "X", "role", "ROLE_ADMIN");

        assertThat(rest.postForEntity("/users/register", admin, String.class).getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(rest.exchange("/users/register", HttpMethod.POST, as("operator", admin), String.class).getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    // ── Business flows on MySQL ─────────────────────────────────────────────

    @Test
    void searchesCarsByModel() {
        JsonNode page = get("/electric-cars/getByModel?search=model&sort=id,asc", "operator");

        assertThat(page.at("/page/totalElements").asInt()).isEqualTo(2);
        assertThat(page.at("/content/0/model").asText()).isEqualTo("Model S");
        assertThat(page.at("/content/0/manufacturerName").asText()).isEqualTo("Tesla");
    }

    @Test
    void revenueByVehicleTypeRunsOnMySql() {
        Map<String, Double> revenue = new HashMap<>();
        get("/statistics/getRevenueByVehicleType", "manager")
                .forEach(r -> revenue.put(r.get("vehicleType").asText(), r.get("totalRevenue").asDouble()));

        assertThat(revenue.get("Electric Car")).isEqualTo(349.38, offset(0.001));
        assertThat(revenue.get("Electric Bicycle")).isEqualTo(18.83, offset(0.001));
        assertThat(revenue.get("Electric Scooter")).isEqualTo(4.59, offset(0.001));
    }

    @Test
    void dailyRevenueAndFaultStatistics() {
        assertThat(get("/statistics/getDailyRevenue?start=2024-03-01T00:00:00&end=2024-03-31T23:59:59", "manager")).hasSize(4);
        assertThat(get("/statistics/getFaultsPerVehicle", "manager")).hasSize(4);
    }

    // February 2025 has no seeded rentals: these tests add their own rides
    // there and remove them again, so the other revenue totals stay as seeded.
    void addRide(String start, String end, Double price) {
        jdbc.update("INSERT INTO rental (rentalStart, rentalEnd, price, client_id, vehicle_id) "
                + "SELECT ?, ?, ?, 4, id FROM transport_vehicle WHERE uniqueIdentifier = 'BIC-001'", start, end, price);
    }

    void removeAddedRides() {
        jdbc.update("DELETE FROM rental WHERE rentalStart >= '2025-02-01' AND rentalStart < '2025-03-02'");
    }

    Map<String, JsonNode> february2025Revenue() {
        Map<String, JsonNode> days = new HashMap<>();
        get("/statistics/getDailyRevenue?start=2025-02-01T00:00:00&end=2025-03-01T00:00:00", "manager")
                .forEach(d -> days.put(d.get("rentalDay").asText(), d.get("totalRevenue")));
        return days;
    }

    @Test
    void dailyRevenueCountsOnlyFinishedRides() {
        try {
            addRide("2025-02-10 09:00:00", "2025-02-10 10:00:00", 12.0);
            addRide("2025-02-10 15:00:00", null, null);
            // The only ride that day is still running: no revenue yet, not a NULL day.
            addRide("2025-02-14 08:00:00", null, null);

            Map<String, JsonNode> days = february2025Revenue();

            assertThat(days).containsOnlyKeys("2025-02-10");
            assertThat(days.get("2025-02-10").asDouble()).isEqualTo(12.0, offset(0.001));
        } finally {
            removeAddedRides();
        }
    }

    @Test
    void dailyRevenueMonthEndsAtTheStartOfTheNextMonth() {
        try {
            addRide("2025-02-28 21:30:00", "2025-02-28 22:00:00", 7.5);
            addRide("2025-03-01 00:00:00", "2025-03-01 00:30:00", 99.0);

            Map<String, JsonNode> days = february2025Revenue();

            assertThat(days).containsOnlyKeys("2025-02-28");
            assertThat(days.get("2025-02-28").asDouble()).isEqualTo(7.5, offset(0.001));
        } finally {
            removeAddedRides();
        }
    }

    @Test
    void promotionsListIsForManagers() {
        assertThat(call(HttpMethod.GET, "/promotions", "manager").getBody()).contains("Weekend Car Special");
    }

    @Test
    void aVehicleCannotBeRentedTwice() {
        assertThat(call(HttpMethod.PUT, "/electric-scooters/rent/7", "operator").getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(call(HttpMethod.PUT, "/electric-scooters/rent/7", "operator").getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    ResponseEntity<String> uploadCsv(String csv) {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("csv", new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8)) {
            @Override
            public String getFilename() {
                return "vehicles.csv";
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token("admin"));
        return rest.postForEntity("/parse-vehicle", new HttpEntity<>(form, headers), String.class);
    }

    @Test
    void importsVehiclesFromCsv() {
        ResponseEntity<String> response = uploadCsv("""
                uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,maxSpeed
                SCO-IT-1,3,Imported Test Scooter,600,4,30
                SCO-IT-2,99,Ghost,600,4,30
                """);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = read(response.getBody());
        assertThat(body.get("parsedVehicles")).hasSize(1);
        assertThat(body.at("/errors/0/rowNumber").asInt()).isEqualTo(2);
        assertThat(get("/electric-scooters/getByModel?search=Imported Test Scooter", "admin").at("/page/totalElements").asInt()).isEqualTo(1);
    }

    @Test
    void aDuplicateIdentifierIsReportedWithoutLosingTheOtherRows() {
        ResponseEntity<String> response = uploadCsv("""
                uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,rangePerCharge
                BIC-001,3,Duplicate of a seeded bike,1500,7,120
                BIC-IT-NEW,3,Fresh Bike,1500,7,120
                """);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = read(response.getBody());
        assertThat(body.get("parsedVehicles")).hasSize(1);
        assertThat(body.at("/errors/0/rowNumber").asInt()).isEqualTo(1);
        assertThat(get("/electric-bicycles/getByModel?search=Fresh Bike", "admin").at("/page/totalElements").asInt()).isEqualTo(1);
    }

    @Test
    void openApiDocumentListsTheEndpoints() {
        JsonNode paths = get("/v3/api-docs", null).get("paths");
        List<String> names = new java.util.ArrayList<>();
        paths.fieldNames().forEachRemaining(names::add);

        assertThat(names).contains("/auth/login", "/auth/me", "/electric-cars", "/parse-vehicle", "/statistics/getDailyRevenue");
    }

    // ── Fleet overview, map and prices ──────────────────────────────────────

    long vehicleId(String uniqueIdentifier) {
        return get("/vehicles?search=" + uniqueIdentifier, "manager").at("/content/0/id").asLong();
    }

    @Test
    void theFleetOverviewListsEveryTypeWithStatusAndPosition() {
        JsonNode page = get("/vehicles?size=50&sort=id,asc", "operator");
        List<String> types = new java.util.ArrayList<>();
        page.get("content").forEach(v -> types.add(v.get("type").asText()));

        assertThat(types).contains("CAR", "BICYCLE", "SCOOTER");
        JsonNode first = page.at("/content/0");
        assertThat(first.get("uniqueIdentifier").asText()).isEqualTo("CAR-001");
        assertThat(first.get("manufacturer").asText()).isEqualTo("Tesla");
        assertThat(first.get("latitude").asDouble()).isEqualTo(44.7722, offset(1e-6));
        assertThat(get("/vehicles?search=BIC-003", "operator").at("/content/0/broken").asBoolean()).isTrue();
    }

    @Test
    void theMapGetsEveryPositionedVehicle() {
        JsonNode located = get("/vehicles/locations", "operator");

        assertThat(located.size()).isGreaterThanOrEqualTo(10);
        located.forEach(v -> assertThat(v.get("latitude").isNull()).isFalse());
    }

    @Test
    void onlyManagersChangeRentPrices() {
        long id = vehicleId("SCO-002");
        HttpHeaders json = new HttpHeaders();
        json.setContentType(MediaType.APPLICATION_JSON);

        HttpHeaders asAdmin = new HttpHeaders(json);
        asAdmin.setBearerAuth(token("admin"));
        assertThat(rest.exchange("/vehicles/" + id + "/rent-price", HttpMethod.PUT,
                new HttpEntity<>(Map.of("rentPrice", 9.5), asAdmin), String.class).getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);

        HttpHeaders asManager = new HttpHeaders(json);
        asManager.setBearerAuth(token("manager"));
        assertThat(rest.exchange("/vehicles/" + id + "/rent-price", HttpMethod.PUT,
                new HttpEntity<>(Map.of("rentPrice", -1), asManager), String.class).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseEntity<String> ok = rest.exchange("/vehicles/" + id + "/rent-price", HttpMethod.PUT,
                new HttpEntity<>(Map.of("rentPrice", 9.5), asManager), String.class);
        assertThat(ok.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(get("/electric-scooters/" + id, "operator").get("rentPrice").asDouble()).isEqualTo(9.5);
    }

    // ── Search on every list ────────────────────────────────────────────────

    @Test
    void employeeSearchFiltersByUsername() {
        JsonNode page = get("/users/getAllByUsername?search=mana", "admin");

        assertThat(page.at("/page/totalElements").asInt()).isEqualTo(1);
        assertThat(page.at("/content/0/username").asText()).isEqualTo("manager");
    }

    @Test
    void faultsOfAVehicleCanBeSearched() {
        long id = vehicleId("BIC-003");

        assertThat(get("/faults/getAllByVehicleId/" + id + "?search=CHAIN", "operator").at("/page/totalElements").asInt())
                .isEqualTo(1);
        assertThat(get("/faults/getAllByVehicleId/" + id + "?search=battery", "operator").at("/page/totalElements").asInt())
                .isZero();
    }

    @Test
    void rentalsCanBeSearchedByClientOrVehicle() {
        assertThat(get("/rentals?search=client2", "operator").at("/page/totalElements").asInt()).isEqualTo(6);
        JsonNode byCar = get("/rentals?search=CAR-004", "operator");
        assertThat(byCar.at("/page/totalElements").asInt()).isEqualTo(1);
        assertThat(byCar.at("/content/0/clientUsername").asText()).isEqualTo("client2");
        assertThat(byCar.at("/content/0/vehicleUniqueIdentifier").asText()).isEqualTo("CAR-004");
        long car1 = vehicleId("CAR-001");
        assertThat(get("/rentals/getAllByVehicleId/" + car1 + "?search=client1", "manager").at("/page/totalElements").asInt())
                .isEqualTo(2);
    }

    @Test
    void theCombinedRssFeedHasPromotionsAndAnnouncements() {
        ResponseEntity<String> feed = call(HttpMethod.GET, "/rss", null);

        assertThat(feed.getHeaders().getContentType().toString()).startsWith("application/rss+xml");
        assertThat(feed.getBody()).contains("<rss", "Monday Scooter Deal", "Holiday Notice");
    }

    @Test
    void aVehicleCanBeCreatedWithoutAnImage() {
        MultipartBodyBuilderHelper form = new MultipartBodyBuilderHelper()
                .add("uniqueIdentifier", "SCO-NOIMG").add("manufacturerId", "3").add("model", "Plain scooter")
                .add("purchasePrice", "400").add("rentPrice", "3").add("maxSpeed", "20");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token("admin"));

        ResponseEntity<String> created = rest.postForEntity("/electric-scooters", new HttpEntity<>(form.body, headers), String.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(read(created.getBody()).get("image").asText()).isEmpty();
    }

    static final class MultipartBodyBuilderHelper {
        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        MultipartBodyBuilderHelper add(String name, String value) {
            body.add(name, value);
            return this;
        }
    }
}
