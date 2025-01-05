package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.Table;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Enabled
@Table(name="electric_scooter")
public class ElectricScooterEntity extends TransportVehicleEntity{
    private double maxSpeed;
}
