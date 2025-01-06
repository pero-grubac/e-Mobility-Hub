package org.unibl.etf.emobility_hub.models.domain.value;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Embeddable
public class Location {
    private double latitude;
    private double longitude;

    public Location() {

    }

    public Location(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
