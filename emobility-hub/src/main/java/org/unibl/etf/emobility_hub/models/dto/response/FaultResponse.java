package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaultResponse {
    private Long id;
    private String description;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
}
