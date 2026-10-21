package org.unibl.etf.emobility_hub_user.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;
import org.unibl.etf.emobility_hub_user.models.entity.DocumentType;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.models.entity.RoleEnum;
import org.unibl.etf.emobility_hub_user.models.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;
import org.unibl.etf.emobility_hub_user.ride.RideException;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

// The client app talks to MySQL directly, so its SQL is tested against a real
// MySQL 8 built from the API's own Flyway migrations, in version order.
// Skipped without Docker.
@Testcontainers(disabledWithoutDocker = true)
class DaoIntegrationTest {
    static final Path DB = Path.of("..", "emobility-hub", "src", "main", "resources", "db");
    static final AtomicInteger CLIENTS = new AtomicInteger();

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("emobility")
            .withCopyFileToContainer(script("migration/V1__schema.sql"), "/docker-entrypoint-initdb.d/01.sql")
            .withCopyFileToContainer(script("seed/V1_1__demo_data.sql"), "/docker-entrypoint-initdb.d/02.sql")
            .withCopyFileToContainer(script("migration/V2__rides_and_vehicle_location.sql"), "/docker-entrypoint-initdb.d/03.sql")
            .withCopyFileToContainer(script("seed/V2_1__demo_locations_and_rides.sql"), "/docker-entrypoint-initdb.d/04.sql");

    static MountableFile script(String path) {
        return MountableFile.forHostPath(DB.resolve(path));
    }

    @BeforeAll
    static void pointThePoolAtTheContainer() {
        // Read once, when ConnectionPool is first loaded by a DAO below.
        System.setProperty("db.url", mysql.getJdbcUrl());
        System.setProperty("db.username", mysql.getUsername());
        System.setProperty("db.password", mysql.getPassword());
    }

    static void sql(String statement, Object... params) throws Exception {
        try (Connection c = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
             PreparedStatement ps = c.prepareStatement(statement)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        }
    }

    static long vehicleId(String uniqueIdentifier) throws Exception {
        try (Connection c = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
             PreparedStatement ps = c.prepareStatement("SELECT id FROM transport_vehicle WHERE uniqueIdentifier = ?")) {
            ps.setString(1, uniqueIdentifier);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    static ClientEntity newClient(String username) {
        ClientEntity client = new ClientEntity();
        client.setUsername(username);
        client.setPassword("s3cret-pass");
        client.setFirstName("Test");
        client.setLastName("Client");
        client.setEmail(username + "@example.com");
        client.setPhoneNumber("+387-" + username);
        client.setIdCardNumber("ID-" + username);
        return client;
    }

    // A fresh client per test, so tests do not share active rides.
    static long registeredClient() {
        ClientEntity client = newClient("rider" + CLIENTS.incrementAndGet());
        assertThat(ClientDAO.createClient(client)).isTrue();
        return client.getId();
    }

    static RentalEntity ride(long clientId, long vehicleId) {
        RentalEntity ride = new RentalEntity();
        ride.setClientId(clientId);
        ride.setVehicleId(vehicleId);
        ride.setRentalStart(LocalDateTime.now().minusMinutes(30));
        ride.setStartLatitude(44.7722);
        ride.setStartLongitude(17.1910);
        ride.setPaymentCardLast4("4242");
        return ride;
    }

    // ── Accounts ────────────────────────────────────────────────────────────

    @Test
    void aSeededClientLogsInWithTheBcryptPassword() {
        ClientEntity client = ClientDAO.getByUsernameAndPassword("client1", "operator");

        assertThat(client).isNotNull();
        assertThat(client.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(client.getRole()).isEqualTo(RoleEnum.ROLE_CLIENT);
    }

    @Test
    void aWrongPasswordOrAnEmployeeAccountIsRejected() {
        assertThat(ClientDAO.getByUsernameAndPassword("client1", "wrong")).isNull();
        assertThat(ClientDAO.getByUsernameAndPassword("manager", "manager")).isNull();
    }

    @Test
    void registrationCreatesAClientThatCanLogIn() {
        assertThat(ClientDAO.createClient(newClient("newbie"))).isTrue();
        assertThat(ClientDAO.usernameExists("newbie")).isTrue();
        assertThat(ClientDAO.createClient(newClient("newbie"))).isFalse();

        assertThat(ClientDAO.getByUsernameAndPassword("newbie", "s3cret-pass")).isNotNull();
    }

    @Test
    void registrationReportsATakenEmailPhoneOrIdCard() {
        ClientEntity copy = newClient("someoneElse");
        copy.setEmail("john.doe@example.com");
        assertThat(ClientDAO.findTakenField(copy)).isEqualTo("email");

        copy = newClient("someoneElse");
        copy.setIdCardNumber("ID-001-JD");
        assertThat(ClientDAO.findTakenField(copy)).isEqualTo("ID card number");

        assertThat(ClientDAO.findTakenField(newClient("someoneElse"))).isNull();
    }

    @Test
    void aBlockedClientCannotLogIn() throws Exception {
        ClientDAO.createClient(newClient("blocked"));
        sql("UPDATE client c JOIN user u ON u.id = c.id SET c.isBlocked = 1 WHERE u.username = ?", "blocked");

        assertThat(ClientDAO.getByUsernameAndPassword("blocked", "s3cret-pass")).isNull();
    }

    @Test
    void aChangedPasswordWorksOnTheNextLogin() {
        ClientEntity client = newClient("changer");
        ClientDAO.createClient(client);

        assertThat(ClientDAO.updatePassword(client.getId(), "brand-new-pass")).isTrue();

        assertThat(ClientDAO.getByUsernameAndPassword("changer", "s3cret-pass")).isNull();
        assertThat(ClientDAO.getByUsernameAndPassword("changer", "brand-new-pass")).isNotNull();
    }

    // ── Vehicles ────────────────────────────────────────────────────────────

    // The client app numbers its pages from 1; broken and rented vehicles are hidden.
    @Test
    void vehicleListsShowOnlyFreeVehicles() {
        assertThat(ElectricCarDAO.getElectricCarsPaginated(1, 2).getPage().getTotalElements()).isEqualTo(4);
        assertThat(ElectricCarDAO.getElectricCarsPaginated(1, 2).getContent()).hasSize(2);
        assertThat(ElectricBicycleDAO.getElectricBicyclesPaginated(1, 10).getContent())
                .extracting(b -> b.getUniqueIdentifier()).containsExactlyInAnyOrder("BIC-001", "BIC-002");
        assertThat(ElectricScooterDAO.getElectricScootersPaginated(1, 10).getContent()).hasSize(3);
    }

    @Test
    void aVehicleIsLoadedWithItsTypeAndPosition() throws Exception {
        TransportVehicleEntity car = TransportVehicleDAO.getById(vehicleId("CAR-001"));
        TransportVehicleEntity bike = TransportVehicleDAO.getById(vehicleId("BIC-003"));

        assertThat(car.getType()).isEqualTo(VehicleType.CAR);
        assertThat(car.getRentPrice()).isEqualTo(50.0);
        assertThat(car.getLatitude()).isEqualTo(44.7722);
        assertThat(bike.getType()).isEqualTo(VehicleType.BICYCLE);
        assertThat(bike.isAvailable()).isFalse();
    }

    @Test
    void reportingAFaultMarksTheVehicleBroken() throws Exception {
        long id = vehicleId("SCO-003");
        try {
            assertThat(FaultDAO.reportFault(id, "Flat tyre")).isTrue();
            assertThat(TransportVehicleDAO.getById(id).isBroken()).isTrue();
        } finally {
            sql("UPDATE transport_vehicle SET isBroken = 0 WHERE id = ?", id);
        }
    }

    // ── Rides ───────────────────────────────────────────────────────────────

    @Test
    void startingARideStoresItAndTakesTheVehicle() throws Exception {
        long client = registeredClient();
        long scooter = vehicleId("SCO-001");
        RentalEntity ride = ride(client, scooter);

        RentalEntity started = RentalDAO.startRide(ride);
        try {
            assertThat(started.isActive()).isTrue();
            assertThat(started.getPaymentCardLast4()).isEqualTo("4242");
            assertThat(started.getVehicleType()).isEqualTo(VehicleType.SCOOTER);
            assertThat(RentalDAO.getActiveRide(client).getId()).isEqualTo(started.getId());
            assertThat(TransportVehicleDAO.getById(scooter).isRented()).isTrue();
        } finally {
            RentalDAO.finishRide(started.getId(), client, 44.77, 17.19, LocalDateTime.now());
        }
    }

    @Test
    void aCarRideKeepsTheDocuments() throws Exception {
        long client = registeredClient();
        RentalEntity ride = ride(client, vehicleId("CAR-004"));
        ride.setDocumentType(DocumentType.PASSPORT);
        ride.setDocumentNumber("P-1234567");
        ride.setDriverLicenseNumber("DL-55555");

        RentalEntity started = RentalDAO.startRide(ride);
        try {
            assertThat(started.getDocumentType()).isEqualTo(DocumentType.PASSPORT);
            assertThat(started.getDocumentNumber()).isEqualTo("P-1234567");
            assertThat(started.getDriverLicenseNumber()).isEqualTo("DL-55555");
        } finally {
            RentalDAO.finishRide(started.getId(), client, 44.77, 17.19, LocalDateTime.now());
        }
    }

    @Test
    void aClientCannotHaveTwoRidesAtOnce() throws Exception {
        long client = registeredClient();
        RentalEntity first = RentalDAO.startRide(ride(client, vehicleId("SCO-002")));
        try {
            assertThatThrownBy(() -> RentalDAO.startRide(ride(client, vehicleId("BIC-001"))))
                    .isInstanceOf(RideException.class).hasMessageContaining("already have a ride");
            assertThat(TransportVehicleDAO.getById(vehicleId("BIC-001")).isRented()).isFalse();
        } finally {
            RentalDAO.finishRide(first.getId(), client, 44.77, 17.19, LocalDateTime.now());
        }
    }

    @Test
    void aRentedOrBrokenVehicleCannotBeTaken() throws Exception {
        long rider = registeredClient();
        long other = registeredClient();
        RentalEntity taken = RentalDAO.startRide(ride(rider, vehicleId("BIC-002")));
        try {
            assertThatThrownBy(() -> RentalDAO.startRide(ride(other, vehicleId("BIC-002"))))
                    .isInstanceOf(RideException.class).hasMessageContaining("no longer available");
            assertThatThrownBy(() -> RentalDAO.startRide(ride(other, vehicleId("BIC-003"))))
                    .isInstanceOf(RideException.class).hasMessageContaining("no longer available");
            assertThat(RentalDAO.getActiveRide(other)).isNull();
        } finally {
            RentalDAO.finishRide(taken.getId(), rider, 44.77, 17.19, LocalDateTime.now());
        }
    }

    @Test
    void finishingARideChargesItAndParksTheVehicle() throws Exception {
        long client = registeredClient();
        long car = vehicleId("CAR-003");   // 45 EUR / hour
        RentalEntity ride = ride(client, car);
        ride.setRentalStart(LocalDateTime.of(2026, 10, 21, 10, 0));
        RentalEntity started = RentalDAO.startRide(ride);

        RentalEntity finished = RentalDAO.finishRide(started.getId(), client, 44.7812, 17.2310,
                LocalDateTime.of(2026, 10, 21, 11, 30));

        assertThat(finished.isActive()).isFalse();
        assertThat(finished.getDuration()).isEqualTo(1.5);
        assertThat(finished.getPrice()).isEqualTo(67.5);
        assertThat(finished.getDistance()).isEqualTo(3.32, offset(0.05));
        TransportVehicleEntity parked = TransportVehicleDAO.getById(car);
        assertThat(parked.isRented()).isFalse();
        assertThat(parked.getLatitude()).isEqualTo(44.7812);
        assertThat(parked.getLongitude()).isEqualTo(17.2310);
        assertThat(RentalDAO.getActiveRide(client)).isNull();
    }

    @Test
    void aRideCanOnlyBeFinishedOnceAndOnlyByItsClient() throws Exception {
        long client = registeredClient();
        long stranger = registeredClient();
        RentalEntity started = RentalDAO.startRide(ride(client, vehicleId("SCO-003")));

        assertThatThrownBy(() -> RentalDAO.finishRide(started.getId(), stranger, 44.77, 17.19, LocalDateTime.now()))
                .isInstanceOf(RideException.class);
        RentalDAO.finishRide(started.getId(), client, 44.77, 17.19, LocalDateTime.now());
        assertThatThrownBy(() -> RentalDAO.finishRide(started.getId(), client, 44.77, 17.19, LocalDateTime.now()))
                .isInstanceOf(RideException.class).hasMessageContaining("not in progress");
    }

    @Test
    void aClientOnlySeesTheirOwnRentals() throws Exception {
        long client4Rental;
        try (Connection c = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
             PreparedStatement ps = c.prepareStatement("SELECT MIN(id) FROM rental WHERE client_id = 4");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            client4Rental = rs.getLong(1);
        }

        assertThat(RentalDAO.getByIdForClient(client4Rental, 4)).isNotNull();
        assertThat(RentalDAO.getByIdForClient(client4Rental, 5)).isNull();
    }

    @Test
    void rentalHistoryIsPerClientWithVehicleDetails() {
        var history = RentalDAO.getPaginatedRentalsByClient(1, 10, 4);

        assertThat(history.getPage().getTotalElements()).isEqualTo(6);
        assertThat(history.getContent()).allSatisfy(r -> {
            assertThat(r.getClientId()).isEqualTo(4);
            assertThat(r.getVehicleModel()).isNotBlank();
        });
        assertThat(RentalDAO.getPaginatedRentalsByClient(1, 10, 5).getPage().getTotalElements()).isEqualTo(6);
    }
}
