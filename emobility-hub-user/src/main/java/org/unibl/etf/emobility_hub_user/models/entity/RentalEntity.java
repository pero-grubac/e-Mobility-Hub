package org.unibl.etf.emobility_hub_user.models.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

// One ride. It is stored when the ride starts; the end time, end location,
// duration, distance and price are filled in when it finishes.
public class RentalEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long clientId;
	private Long vehicleId;
	private LocalDateTime rentalStart;
	private LocalDateTime rentalEnd;
	private double startLatitude;
	private double startLongitude;
	private Double endLatitude;
	private Double endLongitude;
	private Double duration;
	private Double distance;
	private Double price;
	private DocumentType documentType;
	private String documentNumber;
	private String driverLicenseNumber;
	private String paymentCardLast4;

	// Read-only details of the rented vehicle, joined in for display.
	private VehicleType vehicleType;
	private String vehicleModel;
	private String vehicleUniqueIdentifier;
	private String vehicleImage;
	private double rentPrice;

	public boolean isActive() {
		return rentalEnd == null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public LocalDateTime getRentalStart() {
		return rentalStart;
	}

	public void setRentalStart(LocalDateTime rentalStart) {
		this.rentalStart = rentalStart;
	}

	public LocalDateTime getRentalEnd() {
		return rentalEnd;
	}

	public void setRentalEnd(LocalDateTime rentalEnd) {
		this.rentalEnd = rentalEnd;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public Double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(Double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public Double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}

	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}

	public String getPaymentCardLast4() {
		return paymentCardLast4;
	}

	public void setPaymentCardLast4(String paymentCardLast4) {
		this.paymentCardLast4 = paymentCardLast4;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleUniqueIdentifier() {
		return vehicleUniqueIdentifier;
	}

	public void setVehicleUniqueIdentifier(String vehicleUniqueIdentifier) {
		this.vehicleUniqueIdentifier = vehicleUniqueIdentifier;
	}

	public String getVehicleImage() {
		return vehicleImage;
	}

	public void setVehicleImage(String vehicleImage) {
		this.vehicleImage = vehicleImage;
	}

	public double getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(double rentPrice) {
		this.rentPrice = rentPrice;
	}
}
