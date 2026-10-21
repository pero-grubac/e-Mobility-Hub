package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.unibl.etf.emobility_hub.models.domain.value.DocumentType;
import org.unibl.etf.emobility_hub.models.domain.value.Location;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rental")
public class RentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime rentalStart;

    // Empty while the ride is still going.
    private LocalDateTime rentalEnd;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "start_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "start_longitude"))
    })
    private Location startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "end_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "end_longitude"))
    })
    private Location endLocation;

    private Double duration;

    private Double distance;

    private Double price;

    // Required for cars: identity document and driver's licence.
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String documentNumber;

    private String driverLicenseNumber;

    @Column(length = 4)
    private String paymentCardLast4;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private TransportVehicleEntity vehicle;
}
