package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.models.dto.response.ParsedVehicleResponse;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;

import java.io.File;
import java.util.List;

public interface IParseVehicleService {
    ParsedVehicleResponse parse(File file);
}
