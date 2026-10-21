package org.unibl.etf.emobility_hub_user.invoice;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;
import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;
import org.unibl.etf.emobility_hub_user.models.entity.DocumentType;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InvoicePdfTest {

    static ClientEntity client() {
        return new ClientEntity("client1", "x", "John", "Doe", "john.doe@example.com", "+387-65-111-111", "ID-001-JD");
    }

    static RentalEntity finishedCarRide() {
        RentalEntity rental = new RentalEntity();
        rental.setId(42L);
        rental.setVehicleType(VehicleType.CAR);
        rental.setVehicleModel("Model S");
        rental.setVehicleUniqueIdentifier("CAR-001");
        rental.setRentalStart(LocalDateTime.of(2026, 10, 21, 10, 0));
        rental.setRentalEnd(LocalDateTime.of(2026, 10, 21, 11, 30));
        rental.setDuration(1.5);
        rental.setDistance(9.4);
        rental.setStartLatitude(44.7722);
        rental.setStartLongitude(17.191);
        rental.setEndLatitude(44.7801);
        rental.setEndLongitude(17.2201);
        rental.setRentPrice(50);
        rental.setPrice(75.0);
        rental.setPaymentCardLast4("4242");
        rental.setDocumentType(DocumentType.PASSPORT);
        rental.setDocumentNumber("P-7781223");
        rental.setDriverLicenseNumber("DL-9002");
        return rental;
    }

    static String text(RentalEntity rental) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InvoicePdf.write(rental, client(), out);
        PdfReader reader = new PdfReader(out.toByteArray());
        try {
            return PdfTextExtractor.getTextFromPage(reader, 1);
        } finally {
            reader.close();
        }
    }

    @Test
    void listsTheClientVehicleRideAndTotal() throws Exception {
        String text = text(finishedCarRide());

        assertThat(text).contains("Invoice #42", "John Doe (client1)", "Electric car Model S", "CAR-001",
                "21.10.2026 10:00:00", "21.10.2026 11:30:00", "1 h 30 min 00 s", "9.40 km",
                "44.77220, 17.19100", "44.78010, 17.22010", "50.00 EUR / hour", "**** **** **** 4242",
                "Total: 75.00 EUR");
    }

    @Test
    void showsTheDocumentsForCarsOnly() throws Exception {
        assertThat(text(finishedCarRide())).contains("Passport P-7781223", "DL-9002");

        RentalEntity scooterRide = finishedCarRide();
        scooterRide.setVehicleType(VehicleType.SCOOTER);
        assertThat(text(scooterRide)).doesNotContain("Driver's licence").contains("Electric scooter Model S");
    }

    @Test
    void formatsDurations() {
        assertThat(InvoicePdf.formatDuration(0.25)).isEqualTo("0 h 15 min 00 s");
        assertThat(InvoicePdf.formatDuration(2.0 + 1.0 / 3600)).isEqualTo("2 h 00 min 01 s");
    }
}
