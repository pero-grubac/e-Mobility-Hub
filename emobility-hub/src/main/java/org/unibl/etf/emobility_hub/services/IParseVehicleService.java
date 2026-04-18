package org.unibl.etf.emobility_hub.services;

import org.unibl.etf.emobility_hub.models.dto.response.ParsedVehicleResponse;

import java.io.File;

public interface IParseVehicleService {
    ParsedVehicleResponse parse(File file);
}
