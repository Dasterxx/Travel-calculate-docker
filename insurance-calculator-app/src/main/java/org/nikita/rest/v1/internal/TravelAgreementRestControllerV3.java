package org.nikita.rest.v1.internal;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.response.AgreementResponse;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.mapper.AgreementMapper;
import org.nikita.core.security.exceptions.FeedBackMessage;
import org.nikita.core.security.user.UPCUserDetails;
import org.nikita.core.service.agreement.AgreementService;
import org.nikita.core.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/agreement")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class TravelAgreementRestControllerV3 {

    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    public TravelAgreementRestControllerV3(AgreementService agreementService, AgreementMapper agreementMapper) {
        this.agreementService = agreementService;
        this.agreementMapper = agreementMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAgreements(@AuthenticationPrincipal UPCUserDetails userDetails) {
        List<AgreementEntity> agreements;

        // если админ — показываем все
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            agreements = agreementService.findAll();
        } else { // иначе только свои
            agreements = agreementService.findAllByPersonCode(userDetails.getPersonCode());
        }

        List<AgreementDto> responses = agreements
                .stream()
                .map(agreementMapper::toDto)
                .toList();

        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.SUCCESS, responses));
    }

    @GetMapping("/find")
    public ResponseEntity<ApiResponse> findAgreementByUuid(@RequestParam("uuid") String uuidStr,
                                                           @AuthenticationPrincipal UPCUserDetails userDetails) {
        UUID uuid = UUID.fromString(uuidStr);
        AgreementEntity agreement = agreementService.findByUuid(uuid);

        // Если не админ — показывать только если договор принадлежит пользователю
        if (
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
                        agreement.getPersons().stream().noneMatch(p -> p.getPersonCode().equals(userDetails.getPersonCode()))
        ) {
            return ResponseEntity.status(403).body(new ApiResponse(FeedBackMessage.RESOURCE_NOT_FOUND, "Access denied"));
        }

        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.SUCCESS, agreementMapper.toDto(agreement)));
    }
}
