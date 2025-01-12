package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ParsedVehicleResponse {
    List<String> errors;
    List<TransportVehicleResponse> parsedVehicles;
}
