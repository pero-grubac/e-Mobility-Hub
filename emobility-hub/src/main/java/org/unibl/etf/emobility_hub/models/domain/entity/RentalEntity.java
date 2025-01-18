package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.unibl.etf.emobility_hub.models.domain.value.Location;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="rental")
public class RentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime rentalStart;

    @Column(nullable = false)
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

    @Column(nullable = false)
    private double duration;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = true)
    private double price;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private TransportVehicleEntity vehicle;
}
