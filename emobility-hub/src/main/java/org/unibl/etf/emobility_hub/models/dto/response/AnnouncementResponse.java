package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;
}
