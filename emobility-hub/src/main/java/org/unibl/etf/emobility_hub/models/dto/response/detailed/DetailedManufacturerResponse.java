package org.unibl.etf.emobility_hub.models.dto.response.detailed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedManufacturerResponse extends ManufacturerResponse {
    private List<TransportVehicleResponse> vehicles;
}
