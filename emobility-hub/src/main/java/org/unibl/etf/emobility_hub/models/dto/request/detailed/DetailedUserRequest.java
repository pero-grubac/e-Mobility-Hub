package org.unibl.etf.emobility_hub.models.dto.request.detailed;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.models.dto.request.UserRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedUserRequest extends UserRequest {
    @NotNull(message = "Password can not be null")
    @NotEmpty(message = "Password can not be empty")
    String password;
}
