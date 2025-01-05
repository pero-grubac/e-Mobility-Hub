package org.unibl.etf.emobility_hub.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="client")
public class ClientEntity extends UserEntity{
    private String idCardNumber;
    private String avatarImage;
}
