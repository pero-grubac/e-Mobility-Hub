package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectricScooterRequest extends TransportVehicleRequest{
    @Min(value = 1,message = "Max speed must be greater than 1")
    private double maxSpeed;
}
