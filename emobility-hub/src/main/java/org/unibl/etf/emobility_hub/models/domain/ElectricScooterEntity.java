package org.unibl.etf.emobility_hub.models.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="electric_scooter")
public class ElectricScooterEntity extends TransportVehicleEntity{
    private double maxSpeed;
}
