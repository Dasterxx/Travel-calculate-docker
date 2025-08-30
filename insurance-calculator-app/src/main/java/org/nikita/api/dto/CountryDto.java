package org.nikita.api.dto;

import lombok.Data;

@Data
public class CountryDto {
    private String code;
    private String name;

    public CountryDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
