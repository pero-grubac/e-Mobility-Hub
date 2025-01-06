package org.unibl.etf.emobility_hub.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransportVehicleResponse {
    private Long id;
    private String uniqueIdentifier;
    private Manufacturer manufacturer;
    private String model;
    private double purchasePrice;
    private String image;
    private boolean isBroken;
    private boolean isRented;
}
