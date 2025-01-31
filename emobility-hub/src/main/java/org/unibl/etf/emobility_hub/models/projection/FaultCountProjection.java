package org.unibl.etf.emobility_hub.models.projection;

public interface FaultCountProjection {
    Long getVehicleId();
    String getUniqueIdentifier();
    String getModel();
    String getImage();
    Long getFaultCount();
}
