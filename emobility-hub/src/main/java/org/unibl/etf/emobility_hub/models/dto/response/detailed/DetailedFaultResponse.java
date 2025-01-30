package org.unibl.etf.emobility_hub.models.dto.response.detailed;

import lombok.Data;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;

import java.time.LocalDateTime;

@Data
public class DetailedFaultResponse {
    private Long id;
    private String description;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
    private TransportVehicleResponse vehicle;
}
