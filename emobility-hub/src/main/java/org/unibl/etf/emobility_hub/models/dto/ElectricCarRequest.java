package org.unibl.etf.emobility_hub.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectricCarRequest extends TransportVehicleRequest{
    @NotNull(message = "Purchase date can not be null")
    @NotEmpty(message = "Purchase date can not be empty")
    private String purchaseDate;

    @NotNull(message = "Description can not be null")
    @NotEmpty(message = "Description can not be empty")
    private String description;
}
