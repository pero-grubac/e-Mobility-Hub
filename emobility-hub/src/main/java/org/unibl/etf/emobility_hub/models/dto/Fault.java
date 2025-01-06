package org.unibl.etf.emobility_hub.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Fault {
    private Long id;
    private String description;
    private LocalDateTime dateTime;

}
