package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectricCarResponse extends TransportVehicleResponse{
    private LocalDateTime purchaseDate;
    private String description;
}
