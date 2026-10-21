package org.unibl.etf.emobility_hub_user.models.entity;

// Identity document shown when renting a car: an ID card for local citizens,
// a passport for foreigners.
public enum DocumentType {
	ID_CARD("ID card"), PASSPORT("Passport");

	private final String label;

	DocumentType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
