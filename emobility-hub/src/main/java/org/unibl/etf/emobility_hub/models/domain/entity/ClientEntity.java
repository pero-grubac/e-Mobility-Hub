package org.unibl.etf.emobility_hub.models.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="client")
public class ClientEntity extends UserEntity{
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String idCardNumber;
    private String avatarImage;

    private boolean isDeactivated;
    private boolean isBlocked;
}
