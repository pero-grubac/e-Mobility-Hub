package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import org.unibl.etf.emobility_hub.models.domain.value.Location;

import java.time.LocalDateTime;

@Data
public class RentalResponse {
    private Long id;
    private LocalDateTime rentalStart;
    private LocalDateTime rentalEnd;
    private double duration;
    private double distance;
    private double price;
    private Location startLocation;
    private Location endLocation;
    private Long vehicleId;
}
