package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="employee")
public class EmployeeEntity extends UserEntity {
    @Column(nullable = false)
    private String role;
}
