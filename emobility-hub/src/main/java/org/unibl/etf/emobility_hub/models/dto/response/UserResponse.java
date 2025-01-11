package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private RoleEnum role;
}
