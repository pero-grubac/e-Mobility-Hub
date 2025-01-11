package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.base.request.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRequest extends BaseRequest<Long> {
    @NotNull(message = "Username can not be null")
    @NotEmpty(message = "Username can not be empty")
    private String username;

    @NotNull(message = "First name can not be null")
    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @NotEmpty(message = "Last name can not be empty")
    private String lastName;
}
