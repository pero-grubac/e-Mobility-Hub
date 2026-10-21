package org.unibl.etf.emobility_hub_user.controller;

import org.junit.jupiter.api.Test;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;
import org.unibl.etf.emobility_hub_user.models.entity.DocumentType;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.models.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// The server-side rules behind the rent and registration forms.
class ClientControllerValidationTest {
    static final YearMonth NOW = YearMonth.of(2026, 10);

    static TransportVehicleEntity vehicle(VehicleType type) {
        TransportVehicleEntity vehicle = new TransportVehicleEntity();
        vehicle.setId(1L);
        vehicle.setType(type);
        return vehicle;
    }

    static Map<String, String> validScooterForm() {
        Map<String, String> form = new HashMap<>();
        form.put("startLatitude", "44.7722");
        form.put("startLongitude", "17.1910");
        form.put("cardHolder", "John Doe");
        form.put("cardNumber", "4242 4242 4242 4242");
        form.put("cardExpiry", "12/28");
        form.put("cardCvv", "123");
        return form;
    }

    @Test
    void aValidScooterRideKeepsOnlyTheLastFourCardDigits() {
        RentalEntity ride = new RentalEntity();

        List<String> errors = ClientController.validateRide(validScooterForm()::get, vehicle(VehicleType.SCOOTER), ride, NOW);

        assertThat(errors).isEmpty();
        assertThat(ride.getStartLatitude()).isEqualTo(44.7722);
        assertThat(ride.getPaymentCardLast4()).isEqualTo("4242");
        assertThat(ride.getDocumentType()).isNull();
    }

    @Test
    void aCarNeedsAnIdentityDocumentAndALicence() {
        Map<String, String> form = validScooterForm();
        RentalEntity ride = new RentalEntity();

        assertThat(ClientController.validateRide(form::get, vehicle(VehicleType.CAR), ride, NOW)).hasSize(3);

        form.put("documentType", "PASSPORT");
        form.put("documentNumber", "P-7781223");
        form.put("driverLicenseNumber", "DL-90021");
        assertThat(ClientController.validateRide(form::get, vehicle(VehicleType.CAR), ride, NOW)).isEmpty();
        assertThat(ride.getDocumentType()).isEqualTo(DocumentType.PASSPORT);
        assertThat(ride.getDriverLicenseNumber()).isEqualTo("DL-90021");
    }

    @Test
    void reportsEveryBadPaymentAndLocationField() {
        Map<String, String> form = validScooterForm();
        form.put("startLatitude", "123");
        form.put("cardHolder", " ");
        form.put("cardNumber", "4242 4242 4242 4241");
        form.put("cardExpiry", "09/26");
        form.put("cardCvv", "12");

        assertThat(ClientController.validateRide(form::get, vehicle(VehicleType.SCOOTER), new RentalEntity(), NOW))
                .hasSize(5);
    }

    static ClientEntity validClient() {
        return new ClientEntity("newbie", "longenough", "New", "Bie", "new@example.com", "+387-65-000-000", "ID-999-NB");
    }

    @Test
    void aValidRegistrationHasNoErrors() {
        assertThat(ClientController.validateRegistration(validClient(), "longenough")).isEmpty();
    }

    @Test
    void registrationChecksEveryField() {
        ClientEntity client = new ClientEntity("a b", "short", "", "Bie", "not-an-email", "phone", "1");

        assertThat(ClientController.validateRegistration(client, "short")).hasSize(6);
        assertThat(ClientController.validateRegistration(validClient(), "different"))
                .containsExactly("The passwords do not match.");
    }
}
