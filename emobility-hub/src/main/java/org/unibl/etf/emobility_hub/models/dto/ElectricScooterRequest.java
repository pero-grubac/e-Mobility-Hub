package org.unibl.etf.emobility_hub.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectricScooterRequest extends TransportVehicleRequest{
    private double maxSpeed;
}
