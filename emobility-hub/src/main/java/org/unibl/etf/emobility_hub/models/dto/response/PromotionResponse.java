package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PromotionResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
}
