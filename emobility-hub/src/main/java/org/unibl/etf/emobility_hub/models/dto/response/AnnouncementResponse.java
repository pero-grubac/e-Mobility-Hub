package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
