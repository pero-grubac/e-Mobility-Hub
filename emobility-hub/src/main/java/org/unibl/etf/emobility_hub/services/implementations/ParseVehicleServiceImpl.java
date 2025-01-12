package org.unibl.etf.emobility_hub.services.implementations;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricBicycleEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricCarEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricScooterEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricBicycleRequest;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricCarRequest;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricScooterRequest;
import org.unibl.etf.emobility_hub.models.dto.request.TransportVehicleRequest;
import org.unibl.etf.emobility_hub.models.dto.response.*;
import org.unibl.etf.emobility_hub.repositories.ElectricBicycleEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ElectricCarEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ElectricScooterEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IParseVehicleService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Transactional
public class ParseVehicleServiceImpl implements IParseVehicleService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ElectricBicycleEntityRepository electricBicycleEntityRepository;

    @Autowired
    private ElectricCarEntityRepository electricCarEntityRepository;

    @Autowired
    private ElectricScooterEntityRepository electricScooterEntityRepository;

    @Autowired
    private ManufacturerEntityRepository manufacturerEntityRepository;

    @Override
    public ParsedVehicleResponse parse(File file) {
        ParsedVehicleResponse response = new ParsedVehicleResponse();
        List<String> errors = new ArrayList<>();
        List<TransportVehicleResponse> parsedVehicles = new ArrayList<>();

        try (Reader reader = new FileReader(file);
             CSVReader csvReader = new CSVReader(reader)) {

            // Čitanje prvog reda (nazivi kolona)
            String[] headers = csvReader.readNext();
            if (headers == null) {
                errors.add("CSV file is empty or invalid.");
                response.setErrors(errors);
                return response;
            }

            // Čitanje redova podataka
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                try {
                    Map<String, String> rowData = mapRow(headers, line);
                    TransportVehicleRequest request = mapToRequest(rowData);
                    validateRequest(request);
                    TransportVehicleResponse transportVehicleResponse = saveVehicle(request);
                    parsedVehicles.add(transportVehicleResponse);
                } catch (Exception e) {
                    errors.add("Error processing row: " + Arrays.toString(line) + " - " + e.getMessage());
                }
            }
        } catch (IOException | CsvValidationException e) {
            errors.add("Error reading CSV file: " + e.getMessage());
        }

        response.setErrors(errors);
        response.setParsedVehicles(parsedVehicles);
        return response;
    }

    private Map<String, String> mapRow(String[] headers, String[] values) {
        Map<String, String> rowData = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            rowData.put(headers[i], i < values.length ? values[i] : null);
        }
        return rowData;
    }

    private Double parseDouble(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be empty.");
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value, e);
        }
    }


    private Long parseLong(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be empty.");
        }
        try {
            return  Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value);
        }
    }


    private String parseDateTime(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Date value cannot be null or empty.");
        }
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(value, inputFormatter);
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + value, e);
        }
    }


    private TransportVehicleRequest mapToRequest(Map<String, String> rowData) {
        boolean isBicycle = rowData.containsKey("rangePerCharge") && rowData.get("rangePerCharge") != null;
        boolean isCar = rowData.containsKey("purchaseDate") && rowData.get("purchaseDate") != null;
        boolean isScooter = rowData.containsKey("maxSpeed") && rowData.get("maxSpeed") != null;

        // Provera konflikata
        int typeCount = (isBicycle ? 1 : 0) + (isCar ? 1 : 0) + (isScooter ? 1 : 0);
        if (typeCount > 1) {
            throw new IllegalArgumentException("Conflicting attributes in row: " + rowData);
        }

        // Mapiranje na odgovarajući tip vozila
        TransportVehicleRequest request;
        if (isBicycle) {
            ElectricBicycleRequest bicycleRequest = new ElectricBicycleRequest();
            bicycleRequest.setRangePerCharge(parseDouble(rowData.get("rangePerCharge")));
            request = bicycleRequest;
        } else if (isCar) {
            ElectricCarRequest carRequest = new ElectricCarRequest();
            carRequest.setPurchaseDate(parseDateTime(rowData.get("purchaseDate")));
            carRequest.setDescription(rowData.get("description"));
            request = carRequest;
        } else if (isScooter) {
            ElectricScooterRequest scooterRequest = new ElectricScooterRequest();
            scooterRequest.setMaxSpeed(parseDouble(rowData.get("maxSpeed")));
            request = scooterRequest;
        } else {
            throw new IllegalArgumentException("Unable to determine vehicle type for row: " + rowData);
        }

        // Postavljanje zajedničkih atributa
        request.setUniqueIdentifier(rowData.get("uniqueIdentifier"));
        request.setManufacturerId(parseLong(rowData.get("manufacturerId")));
        request.setModel(rowData.get("model"));
        request.setPurchasePrice(parseDouble(rowData.get("purchasePrice")));
        request.setRentPrice(parseDouble(rowData.get("rentPrice")));

        return request;
    }

    private void validateRequest(TransportVehicleRequest request) {
        // Validacija zajedničkih i specifičnih atributa
        if (request.getUniqueIdentifier() == null || request.getUniqueIdentifier().isEmpty()) {
            throw new IllegalArgumentException("Unique identifier is required.");
        }
        if (request.getManufacturerId() == null) {
            throw new IllegalArgumentException("Manufacturer ID is required.");
        }
        if (request.getModel() == null || request.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required.");
        }
    }

    private TransportVehicleResponse saveVehicle(TransportVehicleRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());
        switch (request) {
            case ElectricBicycleRequest bicycleRequest -> {
                ElectricBicycleEntity entity = mapper.map(bicycleRequest, ElectricBicycleEntity.class);
                entity.setManufacturer(manufacturerEntity);
                electricBicycleEntityRepository.saveAndFlush(entity);
                return mapper.map(entity, ElectricBicycleResponse.class);
            }
            case ElectricCarRequest carRequest -> {
                ElectricCarEntity entity = mapper.map(carRequest, ElectricCarEntity.class);
                entity.setManufacturer(manufacturerEntity);
                entity.setPurchaseDate(LocalDateTime.parse(carRequest.getPurchaseDate()));
                electricCarEntityRepository.saveAndFlush(entity);
                return mapper.map(entity, ElectricCarResponse.class);
            }
            case ElectricScooterRequest scooterRequest -> {
                ElectricScooterEntity entity = mapper.map(scooterRequest, ElectricScooterEntity.class);
                entity.setManufacturer(manufacturerEntity);
                electricScooterEntityRepository.save(entity);
                return mapper.map(entity, ElectricScooterResponse.class);
            }
            default -> {
                throw new IllegalArgumentException("Unknown vehicle type: " + request.getClass().getSimpleName());
            }
        }
    }
    private ManufacturerEntity findManufacturerById(Long id) {
        ManufacturerEntity entity = manufacturerEntityRepository.findById(id).orElse(null);
        if (entity == null)
            throw new EntityNotFoundException("Manufacturer with ID " + id + " not found");
        return entity;
    }
}
