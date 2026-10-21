package org.unibl.etf.emobility_hub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.models.dto.response.ParsedVehicleResponse;
import org.unibl.etf.emobility_hub.services.IParseVehicleService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/parse-vehicle")
@RequiredArgsConstructor
public class ParseVehicleController {
    private final IParseVehicleService service;


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
