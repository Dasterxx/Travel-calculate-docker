package org.nikita.rest.v1.internal;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.CountryDto;
import org.nikita.core.service.CountryService;
import org.nikita.core.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class TravelCountryRestController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCountries() {
        List<CountryDto> countries = countryService.getAllCountries();
        return ResponseEntity.ok(new ApiResponse("success", countries));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse> getCountryByCode(@PathVariable String code) {
        try {
            CountryDto country = countryService.getCountryByCode(code);
            return ResponseEntity.ok(new ApiResponse("success", country));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("error", "Country not found"));
        }
    }
}

