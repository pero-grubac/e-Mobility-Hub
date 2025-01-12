package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ParsedVehicleResponse {
    List<ParseError> errors;
    List<TransportVehicleResponse> parsedVehicles;
}
