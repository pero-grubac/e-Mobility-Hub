package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="employee")
public class EmployeeEntity extends UserEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
