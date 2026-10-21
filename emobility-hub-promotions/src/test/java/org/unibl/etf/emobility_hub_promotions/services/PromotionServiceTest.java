package org.unibl.etf.emobility_hub_promotions.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unibl.etf.emobility_hub_promotions.beans.PromotionResponseBean;
import org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.PromotionRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Contract tests: the promotions app must call the Spring Boot API the way
// PromotionController expects, with the manager's token.
class PromotionServiceTest {
    static final String PAGE = """
            {"content":[{"id":1,"title":"Weekend Car Special","content":"20% off",
                         "startDate":"2024-12-01T00:00:00","endDate":"2024-12-31T23:59:59"}],
             "page":{"size":5,"number":0,"totalElements":1,"totalPages":1}}
            """;

    FakeApi api;
    PromotionService service;

    @BeforeEach
    void start() throws Exception {
        api = new FakeApi();
        service = new PromotionService(api.url("/promotions"));
    }

    @AfterEach
    void stop() {
        api.close();
    }

    static PromotionRequest request() {
        PromotionRequest request = new PromotionRequest();
        request.setTitle("Spring deal");
        request.setContent("10% off");
        request.setStartDate("2025-03-01 00:00");
        request.setEndDate("2025-03-31 23:59");
        return request;
    }

    @Test
    void listsPromotionsWithPagingSearchAndToken() throws Exception {
        api.respond(200, PAGE);

        PaginatedResponse<PromotionResponseBean> page = service.getPromotions("jwt", 0, 5, "car deal");

        assertThat(api.last().uri()).isEqualTo("/promotions/getAll?page=0&size=5&sort=id,desc&search=car+deal");
        assertThat(api.last().authorization()).isEqualTo("Bearer jwt");
        assertThat(page.getPage().getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getStartDate()).isEqualTo(LocalDateTime.of(2024, 12, 1, 0, 0));
    }

    @Test
    void createSendsIsoDates() throws Exception {
        api.respond(200, "{\"id\":9,\"title\":\"Spring deal\"}");

        service.createPromotion("jwt", request());

        assertThat(api.last().method()).isEqualTo("POST");
        JsonNode sent = new ObjectMapper().readTree(api.last().body());
        assertThat(sent.get("startDate").asText()).isEqualTo("2025-03-01T00:00");
        assertThat(sent.get("endDate").asText()).isEqualTo("2025-03-31T23:59");
    }

    @Test
    void updateSendsTheIdInTheBody() throws Exception {
        api.respond(200, "{\"id\":4,\"title\":\"Spring deal\"}");

        PromotionResponseBean updated = service.updatePromotion("jwt", 4L, request());

        assertThat(api.last().method()).isEqualTo("PUT");
        assertThat(new ObjectMapper().readTree(api.last().body()).get("id").asLong()).isEqualTo(4);
        assertThat(updated.getId()).isEqualTo(4);
    }

    @Test
    void deleteExpectsNoContent() throws Exception {
        api.respond(204, "");
        service.deletePromotion("jwt", 3L);
        assertThat(api.last().method()).isEqualTo("DELETE");
        assertThat(api.last().uri()).isEqualTo("/promotions/3");

        api.respond(403, "");
        assertThatThrownBy(() -> service.deletePromotion("jwt", 3L)).hasMessageContaining("403");
    }

    @Test
    void anExpiredSessionSurfacesAsAnError() {
        api.respond(401, "");

        assertThatThrownBy(() -> service.getPromotions("expired", 0, 5, null)).hasMessageContaining("401");
    }
}
