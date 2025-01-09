package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FaultRequest {
    private Long id;

    @NotNull(message = "Description can not be null")
    @NotEmpty(message = "Description can not be empty")
    private String description;

    private Long vehicleId;
}
