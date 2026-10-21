package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// One row of the fleet overview, the map and the pricing page: every vehicle
// type in one shape, with its current status and position.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSummaryResponse {
    private Long id;
    private String type;
    private String uniqueIdentifier;
    private String model;
    private String manufacturer;
    private double purchasePrice;
    private double rentPrice;
    private boolean broken;
    private boolean rented;
    private Double latitude;
    private Double longitude;
    private String image;
}
