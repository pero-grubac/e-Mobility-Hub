package org.unibl.etf.emobility_hub.base.request;

import lombok.Data;

@Data
public class BaseRequest<T> {
    private T id;
}
