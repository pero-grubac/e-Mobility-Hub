package org.unibl.etf.emobility_hub_promotions.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FormValidationTest {

    @Test
    void aCompletePromotionIsValid() {
        assertThat(FormValidation.validatePromotion("Spring deal", "10% off", "2025-03-01T00:00", "2025-03-31T23:59"))
                .isEmpty();
    }

    @Test
    void everyPromotionFieldIsRequired() {
        assertThat(FormValidation.validatePromotion(" ", null, "", null)).containsExactly(
                "Title is required.", "Description is required.", "Start date is required.", "End date is required.");
    }

    @Test
    void thePromotionMustEndAfterItStarts() {
        assertThat(FormValidation.validatePromotion("T", "C", "2025-03-31T10:00", "2025-03-31T10:00"))
                .containsExactly("End date must be after the start date.");
        assertThat(FormValidation.validatePromotion("T", "C", "2025-04-01T00:00", "2025-03-01T00:00"))
                .containsExactly("End date must be after the start date.");
    }

    @Test
    void datesMustUseTheDateTimeInputFormat() {
        assertThat(FormValidation.validatePromotion("T", "C", "01.03.2025", "2025-03-31T23:59"))
                .containsExactly("Start date is not a valid date and time.");
    }

    @Test
    void textLengthsFollowTheDatabaseColumns() {
        String title = "x".repeat(FormValidation.TITLE_MAX + 1);
        String description = "x".repeat(FormValidation.PROMOTION_CONTENT_MAX + 1);

        assertThat(FormValidation.validatePromotion(title, description, "2025-03-01T00:00", "2025-03-02T00:00"))
                .containsExactly("Title can have at most 255 characters.", "Description can have at most 1500 characters.");
        assertThat(FormValidation.validateAnnouncement("T", "x".repeat(FormValidation.ANNOUNCEMENT_CONTENT_MAX)))
                .isEmpty();
        assertThat(FormValidation.validateAnnouncement("T", "x".repeat(FormValidation.ANNOUNCEMENT_CONTENT_MAX + 1)))
                .containsExactly("Content can have at most 255 characters.");
    }

    @Test
    void anAnnouncementNeedsTitleAndContent() {
        assertThat(FormValidation.validateAnnouncement("", "  ")).containsExactly("Title is required.", "Content is required.");
    }
}
