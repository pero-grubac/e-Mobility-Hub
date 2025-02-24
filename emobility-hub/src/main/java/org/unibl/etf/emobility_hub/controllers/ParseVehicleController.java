package org.unibl.etf.emobility_hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.models.dto.response.ParsedVehicleResponse;
import org.unibl.etf.emobility_hub.services.IParseVehicleService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/parse-vehicle")
public class ParseVehicleController {
    @Autowired
    private IParseVehicleService service;


    @PostMapping
    public ResponseEntity<ParsedVehicleResponse> parseVehicles(@RequestParam("csv") MultipartFile csv) throws IOException {
        if (csv == null || csv.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }


            File tempFile = File.createTempFile("uploaded", ".csv");
            csv.transferTo(tempFile);

            ParsedVehicleResponse response = service.parse(tempFile);

          tempFile.delete();

            return ResponseEntity.ok(response);

    }

}
