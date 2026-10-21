package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

@Data
public class TransportVehicleResponse {
    private Long id;
    private String uniqueIdentifier;
    private String model;
    // ModelMapper fills it from manufacturer.name
    private String manufacturerName;
    private double purchasePrice;
    private String image;
    private boolean isBroken;
    private boolean isRented;
    private double rentPrice;
    private Double latitude;
    private Double longitude;
}
