package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="electric_car")
public class ElectricCarEntity extends TransportVehicleEntity {
    private LocalDate purchaseDate;
    private String description;
}
