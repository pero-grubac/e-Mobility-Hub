package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromotionRequest extends BaseRequest<Long>{
    @NotNull(message = "Title can not be null")
    @NotEmpty(message = "Title can not be empty")
    private String title;

    @NotNull(message = "Content can not be null")
    @NotEmpty(message = "Content can not be empty")
    private String content;

    @NotNull(message = "Start date can not be null")
    @NotEmpty(message = "Start date can not be empty")
    private String startDate;

    @NotNull(message = "End date can not be null")
    @NotEmpty(message = "End date can not be empty")
    private String endDate;
}
