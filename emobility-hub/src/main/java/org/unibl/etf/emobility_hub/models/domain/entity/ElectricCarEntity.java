package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="electric_car")
public class ElectricCarEntity extends TransportVehicleEntity {
    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false)
    private String description;
}
