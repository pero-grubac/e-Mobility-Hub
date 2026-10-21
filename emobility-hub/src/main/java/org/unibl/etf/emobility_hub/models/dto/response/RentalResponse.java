package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import org.unibl.etf.emobility_hub.models.domain.value.Location;

import java.time.LocalDateTime;

@Data
public class RentalResponse {
    private Long id;
    // ModelMapper fills these from client.username and vehicle.uniqueIdentifier
    private String clientUsername;
    private String vehicleUniqueIdentifier;
    private LocalDateTime rentalStart;
    private LocalDateTime rentalEnd;
    private Double duration;
    private Double distance;
    private Double price;
    private String documentType;
    private String documentNumber;
    private String driverLicenseNumber;
    private String paymentCardLast4;
    private Location startLocation;
    private Location endLocation;
}
