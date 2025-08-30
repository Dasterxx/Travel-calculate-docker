package org.nikita.rest.v1.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.core.security.exceptions.FeedBackMessage;
import org.nikita.core.service.agreement.AgreementService;
import org.nikita.core.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/agreements")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminAgreementController {

    private final AgreementService agreementService;

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse> updateAgreement(@PathVariable UUID uuid,
                                                       @Valid @RequestBody TravelCalculatePremiumCoreCommand request) {
        AgreementDto dto = agreementService.updateAgreement(uuid, request);
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.AGREEMENT_UPDATE_SUCCESS, dto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable UUID uuid) {
        agreementService.deleteAgreement(uuid);
        return ResponseEntity.noContent().build();
    }
}

