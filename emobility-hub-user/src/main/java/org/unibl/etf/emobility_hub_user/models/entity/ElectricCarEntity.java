package org.unibl.etf.emobility_hub_user.models.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ElectricCarEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String uniqueIdentifier;
	private String manufacturerName;
	private String model;
	private double purchasePrice;
	private String image;
	private boolean isBroken;
	private double rentPrice;
	private LocalDateTime purchaseDate;
	private String description;

	public ElectricCarEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ElectricCarEntity(Long id, String uniqueIdentifier, String manufacturerName, String model,
			double purchasePrice, String image, boolean isBroken, double rentPrice, LocalDateTime purchaseDate,
			String description) {
		super();
		this.id = id;
		this.uniqueIdentifier = uniqueIdentifier;
		this.manufacturerName = manufacturerName;
		this.model = model;
		this.purchasePrice = purchasePrice;
		this.image = image;
		this.isBroken = isBroken;
		this.rentPrice = rentPrice;
		this.purchaseDate = purchaseDate;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public double getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(double rentPrice) {
		this.rentPrice = rentPrice;
	}

	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, image, isBroken, manufacturerName, model, purchaseDate, purchasePrice,
				rentPrice, uniqueIdentifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElectricCarEntity other = (ElectricCarEntity) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(image, other.image) && isBroken == other.isBroken
				&& Objects.equals(manufacturerName, other.manufacturerName) && Objects.equals(model, other.model)
				&& Objects.equals(purchaseDate, other.purchaseDate)
				&& Double.doubleToLongBits(purchasePrice) == Double.doubleToLongBits(other.purchasePrice)
				&& Double.doubleToLongBits(rentPrice) == Double.doubleToLongBits(other.rentPrice)
				&& Objects.equals(uniqueIdentifier, other.uniqueIdentifier);
	}

	@Override
	public String toString() {
		return "ElectricCarEntity [id=" + id + ", uniqueIdentifier=" + uniqueIdentifier + ", manufacturerName="
				+ manufacturerName + ", model=" + model + ", purchasePrice=" + purchasePrice + ", image=" + image
				+ ", isBroken=" + isBroken + ", rentPrice=" + rentPrice + ", purchaseDate=" + purchaseDate
				+ ", description=" + description + "]";
	}

}
