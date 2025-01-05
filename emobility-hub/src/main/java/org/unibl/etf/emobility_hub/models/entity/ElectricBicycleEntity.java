package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="electric_bicycle")
public class ElectricBicycleEntity extends TransportVehicleEntity {
    private double rangePerCharge;
}
