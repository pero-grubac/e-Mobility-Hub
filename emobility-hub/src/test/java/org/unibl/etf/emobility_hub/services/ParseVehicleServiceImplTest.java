package org.unibl.etf.emobility_hub.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.unibl.etf.emobility_hub.EMobilityHubApplication;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricBicycleEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricCarEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricScooterEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.response.ParseError;
import org.unibl.etf.emobility_hub.models.dto.response.ParsedVehicleResponse;
import org.unibl.etf.emobility_hub.repositories.ElectricBicycleEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ElectricCarEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ElectricScooterEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.repositories.TransportVehicleRepository;
import org.unibl.etf.emobility_hub.services.implementations.ParseVehicleServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ParseVehicleServiceImplTest {

    static final String CAR_HEADER = "uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,purchaseDate,description";
    static final String BICYCLE_HEADER = "uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,rangePerCharge";
    static final String SCOOTER_HEADER = "uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,maxSpeed";

    @TempDir
    Path tempDir;

    @Spy
    ModelMapper mapper = new EMobilityHubApplication().modelMapper();
    @Mock
    ElectricBicycleEntityRepository bicycleRepository;
    @Mock
    ElectricCarEntityRepository carRepository;
    @Mock
    ElectricScooterEntityRepository scooterRepository;
    @Mock
    ManufacturerEntityRepository manufacturerRepository;
    @Mock
    TransportVehicleRepository transportVehicleRepository;

    @InjectMocks
    ParseVehicleServiceImpl service;

    ManufacturerEntity tesla;

    @BeforeEach
    void setUp() {
        tesla = new ManufacturerEntity();
        tesla.setId(1L);
        tesla.setName("Tesla");
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(tesla));
        when(manufacturerRepository.findById(99L)).thenReturn(Optional.empty());
    }

    File csv(String... lines) throws IOException {
        Path file = Files.createTempFile(tempDir, "vehicles", ".csv");
        Files.writeString(file, String.join("\n", lines));
        return file.toFile();
    }

    @Test
    void importsValidCars() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(CAR_HEADER,
                "CAR-100,1,Model X,90000,60,2024-01-15 00:00:00,Tesla Model X",
                "CAR-101,1,Model Y,60000,40,2024-02-01 08:30:00,Tesla Model Y"));

        assertThat(response.getErrors()).isEmpty();
        assertThat(response.getParsedVehicles()).hasSize(2);

        ArgumentCaptor<ElectricCarEntity> saved = ArgumentCaptor.forClass(ElectricCarEntity.class);
        verify(carRepository, times(2)).saveAndFlush(saved.capture());
        ElectricCarEntity first = saved.getAllValues().getFirst();
        assertThat(first.getUniqueIdentifier()).isEqualTo("CAR-100");
        assertThat(first.getManufacturer()).isSameAs(tesla);
        assertThat(first.getPurchaseDate()).isEqualTo(LocalDateTime.of(2024, 1, 15, 0, 0));
        assertThat(first.getRentPrice()).isEqualTo(60.0);
        assertThat(first.getImage()).isEmpty();
    }

    @Test
    void importsBicyclesAndScooters() throws IOException {
        ParsedVehicleResponse bicycles = service.parse(csv(BICYCLE_HEADER, "BIC-100,1,Mi Bike Pro,1500,7,120"));
        ParsedVehicleResponse scooters = service.parse(csv(SCOOTER_HEADER, "SCO-100,1,Mi Scooter 4,600,4,30"));

        assertThat(bicycles.getErrors()).isEmpty();
        assertThat(scooters.getErrors()).isEmpty();
        verify(bicycleRepository).saveAndFlush(any(ElectricBicycleEntity.class));
        verify(scooterRepository).save(any(ElectricScooterEntity.class));
    }

    @Test
    void keepsGoingAfterABadRowAndReportsItsRowNumber() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(BICYCLE_HEADER,
                "BIC-1,1,Good,1500,7,120",
                "BIC-2,1,Bad price,1500,abc,120",
                "BIC-3,1,Also good,1500,7,90"));

        assertThat(response.getParsedVehicles()).hasSize(2);
        assertThat(response.getErrors()).singleElement().satisfies(error -> {
            assertThat(error.getRowNumber()).isEqualTo(2);
            assertThat(error.getMessage()).contains("Invalid number format: abc");
            assertThat(error.getInvalidData()).containsEntry("uniqueIdentifier", "BIC-2");
        });
    }

    @Test
    void reportsUnknownManufacturer() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(SCOOTER_HEADER, "SCO-1,99,Ghost,600,4,30"));

        assertThat(response.getParsedVehicles()).isEmpty();
        assertThat(response.getErrors()).extracting(ParseError::getMessage)
                .containsExactly("Manufacturer with ID 99 not found");
        verifyNoInteractions(scooterRepository);
    }

    @Test
    void reportsADuplicateIdentifierAndImportsTheRest() throws IOException {
        when(transportVehicleRepository.existsByUniqueIdentifier("BIC-001")).thenReturn(true);

        ParsedVehicleResponse response = service.parse(csv(BICYCLE_HEADER,
                "BIC-001,1,Taken,1500,7,120",
                "BIC-NEW,1,Fresh,1500,7,120"));

        assertThat(response.getParsedVehicles()).hasSize(1);
        assertThat(response.getErrors()).extracting(ParseError::getMessage)
                .containsExactly("Vehicle with identifier BIC-001 already exists.");
    }

    @Test
    void rejectsIsoDatesForCars() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(CAR_HEADER,
                "CAR-1,1,Model S,80000,50,2024-09-20T20:45:45Z,desc"));

        assertThat(response.getErrors()).extracting(ParseError::getMessage)
                .containsExactly("Invalid date format: 2024-09-20T20:45:45Z");
    }

    @Test
    void requiresDescriptionForCars() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(CAR_HEADER, "CAR-1,1,Model S,80000,50,2024-01-01 00:00:00,"));

        assertThat(response.getErrors()).extracting(ParseError::getMessage)
                .containsExactly("Description is required.");
    }

    @Test
    void requiresUniqueIdentifierAndModel() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(SCOOTER_HEADER,
                ",1,Mi Scooter,600,4,30",
                "SCO-2,1,,600,4,30"));

        assertThat(response.getErrors()).extracting(ParseError::getMessage)
                .containsExactly("Unique identifier is required.", "Model is required.");
    }

    @Test
    void rejectsRowsWithoutATypeColumn() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(
                "uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice",
                "X-1,1,Mystery,100,1"));

        assertThat(response.getErrors()).singleElement()
                .satisfies(e -> assertThat(e.getMessage()).startsWith("Unable to determine vehicle type"));
    }

    @Test
    void rejectsRowsThatLookLikeTwoTypes() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(
                "uniqueIdentifier,manufacturerId,model,purchasePrice,rentPrice,rangePerCharge,maxSpeed",
                "X-1,1,Hybrid,100,1,50,25"));

        assertThat(response.getErrors()).singleElement()
                .satisfies(e -> assertThat(e.getMessage()).startsWith("Conflicting attributes"));
    }

    @Test
    void reportsAnEmptyFile() throws IOException {
        ParsedVehicleResponse response = service.parse(csv(""));

        assertThat(response.getErrors()).extracting(ParseError::getMessage)
                .containsExactly("CSV file is empty or invalid.");
    }

    @Test
    void importsTheBundledMockarooCarFile() {
        File mockaroo = Path.of("..", "Mackaroo", "Mock data", "simple car mock data.csv").toFile();
        org.junit.jupiter.api.Assumptions.assumeTrue(mockaroo.exists(), "Mockaroo sample data not available");
        when(manufacturerRepository.findById(anyLong())).thenReturn(Optional.of(tesla));

        ParsedVehicleResponse response = service.parse(mockaroo);

        assertThat(response.getErrors()).isEmpty();
        assertThat(response.getParsedVehicles()).isNotEmpty();
    }
}
