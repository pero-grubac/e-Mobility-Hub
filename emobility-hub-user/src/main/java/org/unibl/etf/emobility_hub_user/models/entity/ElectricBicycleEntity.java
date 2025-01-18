package org.unibl.etf.emobility_hub_user.models.entity;

import java.io.Serializable;
import java.util.Objects;

public class ElectricBicycleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String uniqueIdentifier;
	private String manufacturerName;
	private String model;
	private double purchasePrice;
	private String image;
	private boolean isBroken;
	private double rentPrice;
	private double rangePerCharge;

	public ElectricBicycleEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ElectricBicycleEntity(Long id, String uniqueIdentifier, String manufacturerName, String model,
			double purchasePrice, String image, boolean isBroken, double rentPrice, double rangePerCharge) {
		super();
		this.id = id;
		this.uniqueIdentifier = uniqueIdentifier;
		this.manufacturerName = manufacturerName;
		this.model = model;
		this.purchasePrice = purchasePrice;
		this.image = image;
		this.isBroken = isBroken;
		this.rentPrice = rentPrice;
		this.rangePerCharge = rangePerCharge;
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

	public double getRangePerCharge() {
		return rangePerCharge;
	}

	public void setRangePerCharge(double rangePerCharge) {
		this.rangePerCharge = rangePerCharge;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, image, isBroken, manufacturerName, model, purchasePrice, rangePerCharge, rentPrice,
				uniqueIdentifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElectricBicycleEntity other = (ElectricBicycleEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(image, other.image) && isBroken == other.isBroken
				&& Objects.equals(manufacturerName, other.manufacturerName) && Objects.equals(model, other.model)
				&& Double.doubleToLongBits(purchasePrice) == Double.doubleToLongBits(other.purchasePrice)
				&& Double.doubleToLongBits(rangePerCharge) == Double.doubleToLongBits(other.rangePerCharge)
				&& Double.doubleToLongBits(rentPrice) == Double.doubleToLongBits(other.rentPrice)
				&& Objects.equals(uniqueIdentifier, other.uniqueIdentifier);
	}

	@Override
	public String toString() {
		return "ElectricBicycleEntity [id=" + id + ", uniqueIdentifier=" + uniqueIdentifier + ", manufacturerName="
				+ manufacturerName + ", model=" + model + ", purchasePrice=" + purchasePrice + ", image=" + image
				+ ", isBroken=" + isBroken + ", rentPrice=" + rentPrice + ", rangePerCharge=" + rangePerCharge + "]";
	}

}
