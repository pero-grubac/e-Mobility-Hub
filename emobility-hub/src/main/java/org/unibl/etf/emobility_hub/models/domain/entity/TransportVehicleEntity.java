package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transport_vehicle")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TransportVehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uniqueIdentifier;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private ManufacturerEntity manufacturer;

    @Column(nullable = false)
    private String model;

    private double purchasePrice;

    @Column(nullable = false)
    private String image;

    private boolean isBroken;
    private boolean isRented;

    @Column(nullable = false)
    private double rentPrice;
}
