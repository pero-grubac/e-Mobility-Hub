package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientResponse extends UserResponse {
    private String idCardNumber;
    private String image;
    private String email;
    private String phoneNumber;
}
