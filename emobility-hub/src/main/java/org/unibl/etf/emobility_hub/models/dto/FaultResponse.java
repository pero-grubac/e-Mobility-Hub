package org.unibl.etf.emobility_hub.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaultResponse {
    private Long id;
    private String description;
    private LocalDateTime dateTime;
    private TransportVehicleResponse vehicle;

}
