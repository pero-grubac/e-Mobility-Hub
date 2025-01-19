package org.unibl.etf.emobility_hub.models.dto.response.detailed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricCarResponse;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedElectricCarResponse extends ElectricCarResponse implements IContainManufacturer {
    private ManufacturerResponse manufacturer;

}
