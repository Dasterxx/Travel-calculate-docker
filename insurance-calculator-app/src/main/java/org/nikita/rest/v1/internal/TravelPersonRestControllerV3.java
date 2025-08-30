package org.nikita.rest.v1.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.registration.DeletePasswordDto;
import org.nikita.api.registration.UserUpdateRequest;
import org.nikita.core.security.exceptions.FeedBackMessage;
import org.nikita.core.security.user.UPCUserDetails;
import org.nikita.core.service.PersonService;
import org.nikita.core.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class TravelPersonRestControllerV3 {

    private final PersonService personService;

    @PutMapping("/update")
    @Validated
    public ResponseEntity<ApiResponse> updateUser(
            @AuthenticationPrincipal UPCUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        log.info("Updating user with id: {}", userDetails.getId());
        PersonDto updatedUser = personService.update(userDetails.getId(), updateRequest);
        log.info("User updated successfully");
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.USER_UPDATE_SUCCESS, updatedUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(@AuthenticationPrincipal UPCUserDetails userDetails,
                                                  @RequestBody DeletePasswordDto dto) {
        personService.delete(userDetails.getId(), dto.getPassword());
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.DELETE_USER_SUCCESS, null));

    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticationPrincipal UPCUserDetails userDetails) {
        PersonDto userDto = personService.findUserDtoById(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse("success", userDto));
    }
}
