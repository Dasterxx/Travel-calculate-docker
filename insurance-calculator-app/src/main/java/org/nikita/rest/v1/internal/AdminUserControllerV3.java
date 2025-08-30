package org.nikita.rest.v1.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.service.admin.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserControllerV3 {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllUsers() {
        List<PersonDto> users = adminUserService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{personCode}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String personCode,
            @RequestHeader("X-User-Email") String adminEmail
    ) {
        adminUserService.deleteUser(personCode, adminEmail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{personCode}/blacklist")
    public ResponseEntity<Void> updateBlacklist(
            @PathVariable String personCode,
            @RequestParam boolean blacklisted
    ) {
        adminUserService.setUserBlacklisted(personCode, blacklisted);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{personCode}")
    public ResponseEntity<PersonDto> updateUser(
            @PathVariable String personCode,
            @Validated(PersonDto.Update.class) @RequestBody PersonDto userUpdateDto
    ) {
        PersonDto updatedUser = adminUserService.updateUser(personCode, userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }
}

