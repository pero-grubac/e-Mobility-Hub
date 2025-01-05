package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="transport_vehicle")
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

    private String model;
    private double purchasePrice;
    private String image;
    private boolean isBroken = false;
    private boolean isRented = false;
}
