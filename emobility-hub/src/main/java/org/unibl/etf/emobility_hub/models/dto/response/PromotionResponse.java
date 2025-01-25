package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
