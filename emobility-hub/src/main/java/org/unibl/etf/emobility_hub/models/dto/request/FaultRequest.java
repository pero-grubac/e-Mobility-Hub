package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.unibl.etf.emobility_hub.base.request.BaseRequest;

@Data
public class FaultRequest extends BaseRequest<Long> {
    @NotNull(message = "Description can not be null")
    @NotEmpty(message = "Description can not be empty")
    private String description;

    private Long vehicleId;
}
