package org.unibl.etf.emobility_hub.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectricCarResponse extends TransportVehicleResponse{
    private LocalDate purchaseDate;
    private String description;
}
