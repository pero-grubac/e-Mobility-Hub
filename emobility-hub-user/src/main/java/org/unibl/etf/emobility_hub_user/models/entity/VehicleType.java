package org.unibl.etf.emobility_hub_user.models.entity;

public enum VehicleType {
	CAR("Electric car", "car"), BICYCLE("Electric bicycle", "bicycle"), SCOOTER("Electric scooter", "scooter");

	private final String label;
	// The controller action that lists vehicles of this type.
	private final String action;

	VehicleType(String label, String action) {
		this.label = label;
		this.action = action;
	}

	public String getLabel() {
		return label;
	}

	public String getAction() {
		return action;
	}
}
