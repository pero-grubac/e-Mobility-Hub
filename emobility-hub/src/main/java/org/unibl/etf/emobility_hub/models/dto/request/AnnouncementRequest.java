package org.unibl.etf.emobility_hub.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.base.request.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnnouncementRequest extends BaseRequest<Long> {

    @NotNull(message = "Title can not be null")
    @NotEmpty(message = "Title can not be empty")
    private String title;

    @NotNull(message = "Content can not be null")
    @NotEmpty(message = "Content can not be empty")
    private String content;
}
