package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="fault")
public class FaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    private LocalDateTime updateDateTime;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private TransportVehicleEntity vehicle;
}
