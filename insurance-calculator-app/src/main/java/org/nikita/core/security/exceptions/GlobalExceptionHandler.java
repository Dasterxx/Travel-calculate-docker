package org.nikita.core.security.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.exceptions.AlreadyHasAnAgreement;
import org.nikita.core.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------- Spring Security Authentication exceptions -------------------

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentials(BadCredentialsException e) {
        log.warn("Authentication failed - bad credentials", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Invalid email or password", null));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse> handleLockedException(LockedException e) {
        log.warn("Authentication failed - account locked", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse("User account is locked", null));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> handleDisabledException(DisabledException e) {
        log.warn("Authentication failed - account disabled", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(FeedBackMessage.USER_ACCOUNT_IS_DISABLED, null));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ApiResponse> handleAccountExpiredException(AccountExpiredException e) {
        log.warn("Authentication failed - account expired", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse("User account has expired", null));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ApiResponse> handleCredentialsExpiredException(CredentialsExpiredException e) {
        log.warn("Authentication failed - credentials expired", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse("User credentials have expired", null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException e) {
        log.warn("General authentication failure", e);
        // Здесь можно вернуть либо 401, либо 400 в зависимости от вашего API контракта
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Authentication failed", null));
    }

    // ----------------- Ваши собственные бизнес-исключения -------------------

    @ExceptionHandler(AlreadyHasAnAgreement.class)
    public ResponseEntity<ApiResponse> handleAlreadyHasAnAgreement(AlreadyHasAnAgreement e) {
        log.warn("User already has an agreement", e);
        return ResponseEntity.status(CONFLICT)
                .body(new ApiResponse(FeedBackMessage.ALREADY_HAS_AN_AGREEMENT, null));
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ApiResponse> handleRegistrationException(RegistrationException e) {
        log.warn("Registration error", e);
        return ResponseEntity.badRequest()
                .body(new ApiResponse(FeedBackMessage.REGISTRATION_ERROR, null));
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ApiResponse> handlePasswordException(PasswordException e) {
        log.warn("Incorrect password", e);
        return ResponseEntity.badRequest()
                .body(new ApiResponse(FeedBackMessage.INCORRECT_PASSWORD, null));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiResponse> handleLoginException(LoginException e) {
        log.warn("Login exception", e);
        return ResponseEntity.badRequest()
                .body(new ApiResponse(FeedBackMessage.NO_USER_FOUND, null));
    }

    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ApiResponse> handleVerificationException(VerificationException e) {
        log.warn("Invalid verification token", e);
        return ResponseEntity.badRequest()
                .body(new ApiResponse(FeedBackMessage.INVALID_VERIFICATION_TOKEN, null));
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<ApiResponse> handleUpdateException(UpdateException e) {
        log.warn("User update error", e);
        return ResponseEntity.badRequest()
                .body(new ApiResponse(FeedBackMessage.USER_UPDATE_ERROR, null));
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiResponse> handleRefreshTokenException(RefreshTokenException e) {
        log.warn("Invalid refresh token", e);
        return ResponseEntity.badRequest()
                .body(new ApiResponse(FeedBackMessage.INVALID_VERIFICATION_TOKEN, null));
    }

    // ----------------- Обработка валидационных ошибок -------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        String errorMessage = String.join("; ", errors);

        log.warn("Validation failed: {}", errorMessage);
        return ResponseEntity.badRequest()
                .body(new ApiResponse("Validation error", errors));
    }

    // ----------------- Общий обработчик остальных исключений -------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Internal server error", null));
    }
}
