package org.unibl.etf.emobility_hub.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Manufacturer {
    private Long id;

    @NotNull(message = "Name can not be null")
    @NotEmpty(message = "Name can not be empty")
    private String name;

    @NotNull(message = "Country can not be null")
    @NotEmpty(message = "Country can not be empty")
    private String country;

    @NotNull(message = "Address can not be null")
    @NotEmpty(message = "Address can not be empty")
    private String address;

    @NotNull(message = "Contact phone can not be null")
    @NotEmpty(message = "Contact phone can not be empty")
    private String contactPhone;

    @NotNull(message = "Contact email can not be null")
    @NotEmpty(message = "Contact email can not be empty")
    private String contactEmail;
}
