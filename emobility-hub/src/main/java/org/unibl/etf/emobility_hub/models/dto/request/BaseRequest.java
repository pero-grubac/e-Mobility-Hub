package org.unibl.etf.emobility_hub.models.dto.request;

import lombok.Data;

@Data
public class BaseRequest<T> {
    private T id;
}
