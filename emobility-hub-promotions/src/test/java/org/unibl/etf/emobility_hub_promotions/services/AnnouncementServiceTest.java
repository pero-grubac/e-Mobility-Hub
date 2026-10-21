package org.unibl.etf.emobility_hub_promotions.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unibl.etf.emobility_hub_promotions.beans.AnnouncementResponseBean;
import org.unibl.etf.emobility_hub_promotions.models.dto.PaginatedResponse;
import org.unibl.etf.emobility_hub_promotions.models.dto.request.AnnouncementRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Contract tests: the announcement calls must match AnnouncementController.
class AnnouncementServiceTest {
    static final String ONE = """
            {"id":2,"title":"Holiday Notice","content":"Closed on January 1st",
             "creationDate":"2024-12-20T08:00:00","updateDate":"2024-12-21T09:30:00"}
            """;

    FakeApi api;
    AnnouncementService service;

    @BeforeEach
    void start() throws Exception {
        api = new FakeApi();
        service = new AnnouncementService(api.url("/announcements"));
    }

    @AfterEach
    void stop() {
        api.close();
    }

    static AnnouncementRequest request() {
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle("New vehicles");
        request.setContent("Ten new scooters in the city centre");
        return request;
    }

    @Test
    void listsWithPagingTrimmedSearchAndToken() throws Exception {
        api.respond(200, "{\"content\":[" + ONE + "],\"page\":{\"size\":6,\"number\":1,\"totalElements\":7,\"totalPages\":2}}");

        PaginatedResponse<AnnouncementResponseBean> page = service.getAnnouncements("jwt", 1, 6, "  new year ");

        assertThat(api.last().uri()).isEqualTo("/announcements/getAll?page=1&size=6&sort=id,desc&search=new+year");
        assertThat(api.last().authorization()).isEqualTo("Bearer jwt");
        assertThat(page.getPage().getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0).getUpdateDate()).isEqualTo(LocalDateTime.of(2024, 12, 21, 9, 30));
    }

    @Test
    void listsWithoutASearchParameterWhenTheSearchIsBlank() throws Exception {
        api.respond(200, "{\"content\":[],\"page\":{\"size\":6,\"number\":0,\"totalElements\":0,\"totalPages\":0}}");

        service.getAnnouncements("jwt", 0, 6, "   ");

        assertThat(api.last().uri()).isEqualTo("/announcements/getAll?page=0&size=6&sort=id,desc");
    }

    @Test
    void createPostsTitleAndContent() throws Exception {
        api.respond(200, ONE);

        service.create("jwt", request());

        assertThat(api.last().method()).isEqualTo("POST");
        JsonNode sent = new ObjectMapper().readTree(api.last().body());
        assertThat(sent.get("title").asText()).isEqualTo("New vehicles");
        assertThat(sent.get("content").asText()).isEqualTo("Ten new scooters in the city centre");
    }

    @Test
    void updateSendsTheIdInTheBody() throws Exception {
        api.respond(200, ONE);

        AnnouncementResponseBean updated = service.update("jwt", 2L, request());

        assertThat(api.last().method()).isEqualTo("PUT");
        assertThat(new ObjectMapper().readTree(api.last().body()).get("id").asLong()).isEqualTo(2);
        assertThat(updated.getTitle()).isEqualTo("Holiday Notice");
    }

    @Test
    void deleteExpectsNoContent() throws Exception {
        api.respond(204, "");

        service.delete("jwt", 2L);

        assertThat(api.last().method()).isEqualTo("DELETE");
        assertThat(api.last().uri()).isEqualTo("/announcements/2");
    }

    @Test
    void anExpiredTokenIsReportedAsAnExpiredSession() {
        api.respond(401, "");

        assertThatThrownBy(() -> service.getAnnouncements("expired", 0, 6, null))
                .isInstanceOfSatisfying(ApiException.class, e -> {
                    assertThat(e.getStatus()).isEqualTo(401);
                    assertThat(e.isSessionExpired()).isTrue();
                });
    }

    @Test
    void aMissingAnnouncementIsNotAnExpiredSession() {
        api.respond(404, "");

        assertThatThrownBy(() -> service.getById("jwt", 99L))
                .isInstanceOfSatisfying(ApiException.class, e -> assertThat(e.isSessionExpired()).isFalse());
    }
}
