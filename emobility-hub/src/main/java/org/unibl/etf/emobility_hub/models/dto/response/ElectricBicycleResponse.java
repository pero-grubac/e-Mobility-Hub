package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectricBicycleResponse extends TransportVehicleResponse {
    private double rangePerCharge;
}
