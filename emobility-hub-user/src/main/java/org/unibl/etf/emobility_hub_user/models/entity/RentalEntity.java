package org.unibl.etf.emobility_hub_user.models.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class RentalEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private LocalDateTime rentalStart;
	private LocalDateTime rentalEnd;
	private double end_latitude;
	private double end_longitude;
	private double start_latitude;
	private double start_longitude;
	private double duration;
	private double distance;
	private double price;
	private Long clientId;
	private Long vehicleId;

	public RentalEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RentalEntity(Long id, LocalDateTime rentalStart, LocalDateTime rentalEnd, double end_latitude,
			double end_longitude, double start_latitude, double start_longitude, double duration, double distance,
			double price, Long clientId, Long vehicleId) {
		super();
		this.id = id;
		this.rentalStart = rentalStart;
		this.rentalEnd = rentalEnd;
		this.end_latitude = end_latitude;
		this.end_longitude = end_longitude;
		this.start_latitude = start_latitude;
		this.start_longitude = start_longitude;
		this.duration = duration;
		this.distance = distance;
		this.price = price;
		this.clientId = clientId;
		this.vehicleId = vehicleId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public double getEnd_latitude() {
		return end_latitude;
	}

	public void setEnd_latitude(double end_latitude) {
		this.end_latitude = end_latitude;
	}

	public double getEnd_longitude() {
		return end_longitude;
	}

	public void setEnd_longitude(double end_longitude) {
		this.end_longitude = end_longitude;
	}

	public double getStart_latitude() {
		return start_latitude;
	}

	public void setStart_latitude(double start_latitude) {
		this.start_latitude = start_latitude;
	}

	public double getStart_longitude() {
		return start_longitude;
	}

	public void setStart_longitude(double start_longitude) {
		this.start_longitude = start_longitude;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	@Override
	public int hashCode() {
		return Objects.hash(clientId, distance, duration, end_latitude, end_longitude, id, price, rentalEnd,
				rentalStart, start_latitude, start_longitude, vehicleId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RentalEntity other = (RentalEntity) obj;
		return Objects.equals(clientId, other.clientId)
				&& Double.doubleToLongBits(distance) == Double.doubleToLongBits(other.distance)
				&& Double.doubleToLongBits(duration) == Double.doubleToLongBits(other.duration)
				&& Double.doubleToLongBits(end_latitude) == Double.doubleToLongBits(other.end_latitude)
				&& Double.doubleToLongBits(end_longitude) == Double.doubleToLongBits(other.end_longitude)
				&& Objects.equals(id, other.id)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(rentalEnd, other.rentalEnd) && Objects.equals(rentalStart, other.rentalStart)
				&& Double.doubleToLongBits(start_latitude) == Double.doubleToLongBits(other.start_latitude)
				&& Double.doubleToLongBits(start_longitude) == Double.doubleToLongBits(other.start_longitude)
				&& Objects.equals(vehicleId, other.vehicleId);
	}

	@Override
	public String toString() {
		return "RentalEntity [id=" + id + ", rentalStart=" + rentalStart + ", rentalEnd=" + rentalEnd
				+ ", end_latitude=" + end_latitude + ", end_longitude=" + end_longitude + ", start_latitude="
				+ start_latitude + ", start_longitude=" + start_longitude + ", duration=" + duration + ", distance="
				+ distance + ", price=" + price + ", clientId=" + clientId + ", vehicleId=" + vehicleId + "]";
	}

}
