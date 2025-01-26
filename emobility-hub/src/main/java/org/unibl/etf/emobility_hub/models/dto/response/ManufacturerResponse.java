package org.unibl.etf.emobility_hub.models.dto.response;

import lombok.Data;

@Data
public class ManufacturerResponse {
    private Long id;
    private String name;
    private String country;
    private String address;
    private String contactPhone;
    private String contactEmail;
    private String contactFax;
}
