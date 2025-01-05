package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="fault")
public class FaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    private String description;
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private TransportVehicleEntity vehicle;
}
