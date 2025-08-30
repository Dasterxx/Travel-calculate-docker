package org.nikita.rest.v1.registration;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.api.dto.registration.JwtResponse;
import org.nikita.api.dto.registration.LoginRequest;
import org.nikita.api.dto.registration.TokenRefreshRequest;
import org.nikita.api.dto.registration.TokenRefreshResponse;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.RefreshToken;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.security.exceptions.FeedBackMessage;
import org.nikita.core.security.exceptions.LoginException;
import org.nikita.core.security.exceptions.RegistrationException;
import org.nikita.core.security.jwt.JwtUtils;
import org.nikita.core.security.user.UPCUserDetails;
import org.nikita.core.service.PersonService;
import org.nikita.core.service.register.PasswordResetService;
import org.nikita.core.service.register.RefreshTokenService;
import org.nikita.core.service.register.RegistrationValidationService;
import org.nikita.core.service.register.VerificationTokenService;
import org.nikita.core.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.nikita.core.security.exceptions.FeedBackMessage.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PersonService personService;
    private final VerificationTokenService verificationTokenService;
    private final RefreshTokenService refreshTokenService;
    private final PersonRepository personRepository;
    private final PasswordResetService passwordResetService;
    private final RegistrationValidationService registrationCommandValidator;

    @PostMapping("/login")
    @Validated
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Authenticating user with email: {}", loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenForUser(authentication);
        UPCUserDetails userDetails = (UPCUserDetails) authentication.getPrincipal();

        PersonEntity person = personRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new LoginException("User not found"));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(person);
        log.debug("JWT and refresh token generated for user: {}", userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse(AUTHENTICATION_SUCCESS, new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                refreshToken.getToken()
        )));
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UPCUserDetails userDetails,
                                       HttpServletRequest request) {
        if (userDetails == null) {
            // Можно логировать или отвечать 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PersonEntity user = new PersonEntity();
        user.setId(userDetails.getId());

        refreshTokenService.deleteByUser(user);

        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        log.info("Forgot password request received for email: {}", email);
        passwordResetService.createPasswordResetToken(email);
        log.info("Password reset token created for email: {}", email);
        return ResponseEntity.ok(new ApiResponse(PASSWORD_RESET_EMAIL_SENT, "Password reset email sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        log.info("Resetting password for token: {}", token);
        boolean result = passwordResetService.resetPassword(token, newPassword);
        if (result) {
            log.info("Password reset successful for token: {}", token);
        } else {
            log.warn("Password reset failed for token: {}", token);
        }

        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.PASSWORD_RESET_SUCCESS, "Password reset successfully"));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyAccount(@RequestParam("token") String token) {
        log.info("Verifying account with token: {}", token);

        PersonEntity userOpt = verificationTokenService.validateVerificationToken(token);

        log.info("Account verified for user: {}", userOpt.getEmail());
        return ResponseEntity.ok(new ApiResponse("success", "Account verified successfully"));
    }

    @PostMapping("/register")
    @Validated
    public ResponseEntity<?> registerUser(@RequestBody RegistrationCommand registrationCommand) throws MessagingException {
        log.info("Registering user with email: {}", registrationCommand.getEmail());
        List<ValidationErrorDto> errors = registrationCommandValidator.validate(registrationCommand);
        if (!errors.isEmpty()) {
            log.warn("Registration failed due to validation errors: {}", errors);
            throw new RegistrationException(errors.get(0).description());
        }
        PersonDto userDto = personService.register(registrationCommand);
        return ResponseEntity.ok(new ApiResponse(CREATE_USER_SUCCESS, userDto));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        log.info("Refreshing token for user with refresh token: {}", request.getRefreshToken());
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByTokenOrThrow(requestRefreshToken);
        refreshTokenService.validateRefreshTokenOrThrow(refreshToken);

        PersonEntity user = refreshToken.getUser();

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        UPCUserDetails userDetails = new UPCUserDetails(
                user.getId(),
                user.getPersonCode(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                authorities
        );

        String token = jwtUtils.generateTokenForUser(
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities)
        );

        return ResponseEntity.ok(new ApiResponse("success", new TokenRefreshResponse(token, requestRefreshToken)));
    }


}