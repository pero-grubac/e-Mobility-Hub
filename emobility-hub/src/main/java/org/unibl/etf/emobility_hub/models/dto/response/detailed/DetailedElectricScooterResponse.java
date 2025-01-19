package org.unibl.etf.emobility_hub.models.dto.response.detailed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricScooterResponse;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedElectricScooterResponse extends ElectricScooterResponse implements IContainManufacturer {
    private ManufacturerResponse manufacturer;
}
