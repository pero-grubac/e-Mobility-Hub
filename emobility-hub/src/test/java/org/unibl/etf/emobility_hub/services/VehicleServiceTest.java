package org.unibl.etf.emobility_hub.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.unibl.etf.emobility_hub.EMobilityHubApplication;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricScooterEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricScooterRequest;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricScooterResponse;
import org.unibl.etf.emobility_hub.repositories.ElectricScooterEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.implementations.ElectricScooterServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Exercises BaseVehicleCRUDServiceImpl through the scooter service; cars and
// bicycles share the same rent / fix / brake / image logic.
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @TempDir
    Path uploads;

    @Mock
    ElectricScooterEntityRepository repository;
    @Mock
    ManufacturerEntityRepository manufacturerRepository;

    ElectricScooterServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ElectricScooterServiceImpl(new EMobilityHubApplication().modelMapper(), repository, manufacturerRepository);
        ReflectionTestUtils.setField(service, "baseDir", uploads.toString());
        ReflectionTestUtils.setField(service, "backendUrl", "http://api");
    }

    static ElectricScooterEntity scooter(long id) {
        ManufacturerEntity xiaomi = new ManufacturerEntity();
        xiaomi.setId(3L);
        xiaomi.setName("Xiaomi");
        ElectricScooterEntity scooter = new ElectricScooterEntity();
        scooter.setId(id);
        scooter.setUniqueIdentifier("SCO-" + id);
        scooter.setModel("Mi Scooter");
        scooter.setManufacturer(xiaomi);
        scooter.setImage("");
        scooter.setMaxSpeed(25);
        return scooter;
    }

    static ElectricScooterRequest request() {
        ElectricScooterRequest request = new ElectricScooterRequest();
        request.setUniqueIdentifier("SCO-9");
        request.setManufacturerId(3L);
        request.setModel("Mi Scooter 4");
        request.setPurchasePrice(600);
        request.setRentPrice(4);
        request.setMaxSpeed(30);
        return request;
    }

    @Test
    void rentingAFreeVehicleMarksItRented() {
        ElectricScooterEntity scooter = scooter(6);
        when(repository.findById(6L)).thenReturn(Optional.of(scooter));

        assertThat(service.rent(6L)).isTrue();
        assertThat(scooter.isRented()).isTrue();
        verify(repository).save(scooter);
    }

    @Test
    void rentingARentedVehicleIsRefused() {
        ElectricScooterEntity scooter = scooter(6);
        scooter.setRented(true);
        when(repository.findById(6L)).thenReturn(Optional.of(scooter));

        assertThat(service.rent(6L)).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    void brakeAndFixToggleTheBrokenFlag() {
        ElectricScooterEntity scooter = scooter(6);
        when(repository.findById(6L)).thenReturn(Optional.of(scooter));

        service.brake(6L);
        assertThat(scooter.isBroken()).isTrue();

        service.fix(6L);
        assertThat(scooter.isBroken()).isFalse();
        verify(repository, times(2)).saveAndFlush(scooter);
    }

    @Test
    void detailIncludesTheManufacturer() {
        when(repository.findById(6L)).thenReturn(Optional.of(scooter(6)));

        DetailedElectricScooterResponse response = service.getById(6L);

        assertThat(response.getManufacturer().getName()).isEqualTo("Xiaomi");
        assertThat(response.getMaxSpeed()).isEqualTo(25);
    }

    @Test
    void unknownVehicleIsNotFound() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(404L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("ElectricScooterEntity with ID 404 not found");
    }

    @Test
    void createWithUnknownManufacturerSavesNothing() {
        when(manufacturerRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request())).isInstanceOf(EntityNotFoundException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void createStoresTheImageUnderTheVehicleId() throws Exception {
        when(manufacturerRepository.findById(3L)).thenReturn(Optional.of(scooter(1).getManufacturer()));
        when(repository.saveAndFlush(any())).thenAnswer(inv -> {
            ElectricScooterEntity entity = inv.getArgument(0);
            entity.setId(42L);
            return entity;
        });
        ElectricScooterRequest request = request();
        request.setImage(new MockMultipartFile("image", "scooter.png", "image/png", new byte[]{1, 2, 3}));

        var response = service.create(request);

        assertThat(response.getImage())
                .startsWith("http://api/uploads/transportvehicleentity/electricscooterentity/42/")
                .endsWith("_scooter.png");
        Path dir = uploads.resolve("transportvehicleentity/electricscooterentity/42");
        try (var files = Files.list(dir)) {
            assertThat(files).hasSize(1);
        }
    }

    @Test
    void deleteRemovesTheImageAndItsEmptyFolder() throws Exception {
        Path dir = Files.createDirectories(uploads.resolve("transportvehicleentity/electricscooterentity/6"));
        Files.write(dir.resolve("pic.png"), new byte[]{1});
        ElectricScooterEntity scooter = scooter(6);
        scooter.setImage("http://api/uploads/transportvehicleentity/electricscooterentity/6/pic.png");
        when(repository.findById(6L)).thenReturn(Optional.of(scooter));

        service.delete(6L);

        assertThat(dir).doesNotExist();
        verify(repository).deleteById(6L);
    }
}
