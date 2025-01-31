package org.unibl.etf.emobility_hub.models.dto.response.detailed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.models.dto.response.RentalResponse;
import org.unibl.etf.emobility_hub.models.dto.response.TransportVehicleResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedRentalResponse extends RentalResponse {
    private ClientResponse clientResponse;
    private TransportVehicleResponse transportVehicleResponse;
}
