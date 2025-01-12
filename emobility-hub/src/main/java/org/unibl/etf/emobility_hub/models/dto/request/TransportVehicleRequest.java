package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.base.request.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransportVehicleRequest extends BaseRequest<Long> {
    @NotNull(message = "Unique identifier can not be null")
    @NotEmpty(message = "Unique identifier can not be empty")
    private String uniqueIdentifier;

    @NotNull(message = "Manufacturer id can not be null")
    private Long manufacturerId;

    @NotNull(message = "Model can not be null")
    @NotEmpty(message = "Model can not be empty")
    private String model;

    @NotNull(message = "Purchase price can not be null")
    private double purchasePrice;

    @NotNull(message = "Rent price can not be null")
    private double rentPrice;

    private MultipartFile image;
}
