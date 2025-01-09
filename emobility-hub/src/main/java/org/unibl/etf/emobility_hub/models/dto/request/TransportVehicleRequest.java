package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TransportVehicleRequest {
    private Long id;

    @NotNull(message = "Unique identifier can not be null")
    @NotEmpty(message = "Unique identifier can not be empty")
    private String uniqueIdentifier;

    @NotNull(message = "Manufacturer id can not be null")
    @NotEmpty(message = "Manufacturer id can not be empty")
    private Long manufacturerId;

    @NotNull(message = "Model can not be null")
    @NotEmpty(message = "Model can not be empty")
    private String model;

    @NotNull(message = "Purchase price can not be null")
    @NotEmpty(message = "Purchase price can not be empty")
    private double purchasePrice;

    private MultipartFile image;
}
