package org.unibl.etf.emobility_hub_user.invoice;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.unibl.etf.emobility_hub_user.models.entity.ClientEntity;
import org.unibl.etf.emobility_hub_user.models.entity.RentalEntity;
import org.unibl.etf.emobility_hub_user.models.entity.VehicleType;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

// The bill a client downloads after a finished ride.
public final class InvoicePdf {
	private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	private static final Font TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
	private static final Font LABEL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.DARK_GRAY);
	private static final Font VALUE = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
	private static final Font TOTAL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);

	private InvoicePdf() {
	}

	public static void write(RentalEntity rental, ClientEntity client, OutputStream out) throws DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();

		document.add(new Paragraph("eMobilityHub - Invoice #" + rental.getId(), TITLE));
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		row(table, "Client", client.getFirstName() + " " + client.getLastName() + " (" + client.getUsername() + ")");
		row(table, "Email", client.getEmail());
		row(table, "Vehicle", rental.getVehicleType().getLabel() + " " + rental.getVehicleModel());
		row(table, "Vehicle ID", rental.getVehicleUniqueIdentifier());
		row(table, "Ride start", rental.getRentalStart().format(DATE_TIME));
		row(table, "Ride end", rental.getRentalEnd().format(DATE_TIME));
		row(table, "Duration", formatDuration(rental.getDuration()));
		row(table, "Distance", String.format(Locale.US, "%.2f km", rental.getDistance()));
		row(table, "Pick-up location", coordinates(rental.getStartLatitude(), rental.getStartLongitude()));
		row(table, "Drop-off location", coordinates(rental.getEndLatitude(), rental.getEndLongitude()));
		if (rental.getVehicleType() == VehicleType.CAR && rental.getDocumentType() != null) {
			row(table, "Identity document", rental.getDocumentType().getLabel() + " " + rental.getDocumentNumber());
			row(table, "Driver's licence", rental.getDriverLicenseNumber());
		}
		row(table, "Rate", String.format(Locale.US, "%.2f EUR / hour", rental.getRentPrice()));
		row(table, "Paid with card", "**** **** **** " + rental.getPaymentCardLast4());
		document.add(table);

		Paragraph total = new Paragraph(String.format(Locale.US, "Total: %.2f EUR", rental.getPrice()), TOTAL);
		total.setAlignment(Element.ALIGN_RIGHT);
		total.setSpacingBefore(12);
		document.add(total);

		document.close();
	}

	public static String formatDuration(double hours) {
		long totalSeconds = Math.round(hours * 3600);
		return String.format("%d h %02d min %02d s", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);
	}

	private static String coordinates(double latitude, double longitude) {
		return String.format(Locale.US, "%.5f, %.5f", latitude, longitude);
	}

	private static void row(PdfPTable table, String label, String value) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL));
		PdfPCell valueCell = new PdfPCell(new Phrase(value == null ? "-" : value, VALUE));
		labelCell.setPadding(5);
		valueCell.setPadding(5);
		table.addCell(labelCell);
		table.addCell(valueCell);
	}
}
