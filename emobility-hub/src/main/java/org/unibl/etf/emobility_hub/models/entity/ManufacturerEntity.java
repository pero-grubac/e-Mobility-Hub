package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="manufacturer")
public class ManufacturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String address;

    @Column(unique = true, nullable = false)
    private String contactPhone;

    @Column(unique = true, nullable = false)
    private String contactFax;

    @Column(unique = true, nullable = false)
    private String contactEmail;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransportVehicleEntity> vehicles;
}
