package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientRequest extends UserRequest {
    private MultipartFile image;

    @NotNull(message = "Id card number can not be null")
    @NotEmpty(message = "Id card number can not be empty")
    private String idCardNumber;

    @NotNull(message = "Email can not be null")
    @NotEmpty(message = "Email can not be empty")
    private String email;

    @NotNull(message = "Phone number can not be null")
    @NotEmpty(message = "Phone number can not be empty")
    private String phoneNumber;
}
