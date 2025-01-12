package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ParseError {
    private int rowNumber;
    private String message;
    private Map<String, String> invalidData;
}
