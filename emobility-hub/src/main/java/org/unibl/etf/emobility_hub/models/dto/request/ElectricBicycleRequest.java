package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class   ElectricBicycleRequest extends TransportVehicleRequest{
    @Min(value = 1,message = "Range per charge must be greater than 1")
    private double rangePerCharge;
}
