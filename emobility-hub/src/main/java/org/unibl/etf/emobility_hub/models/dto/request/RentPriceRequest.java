package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentPriceRequest {
    @NotNull(message = "Rent price can not be null")
    @Positive(message = "Rent price must be greater than 0")
    private Double rentPrice;
}
