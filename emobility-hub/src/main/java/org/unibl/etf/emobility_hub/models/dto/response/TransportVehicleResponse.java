package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import org.unibl.etf.emobility_hub.models.dto.request.ManufacturerRequest;

@Data
public class TransportVehicleResponse {
    private Long id;
    private String uniqueIdentifier;
    private ManufacturerRequest manufacturerRequest;
    private String model;
    private double purchasePrice;
    private String image;
    private boolean isBroken;
    private boolean isRented;
}
