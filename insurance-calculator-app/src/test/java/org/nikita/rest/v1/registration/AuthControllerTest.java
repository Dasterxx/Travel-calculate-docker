package org.nikita.rest.v1.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.registration.LoginRequest;
import org.nikita.api.dto.registration.TokenRefreshRequest;
import org.nikita.api.registration.UserUpdateRequest;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.security.exceptions.LoginException;
import org.nikita.core.security.user.UPCUserDetails;
import org.nikita.core.service.PersonService;
import org.nikita.core.service.register.PasswordResetService;
import org.nikita.core.service.register.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PersonService personService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PasswordResetService passwordResetService;

    private LoginRequest loginRequest;
    private RegistrationCommand registrationRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setup() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example");
        loginRequest.setPassword("password");

        // Мокируем personRepository.findByEmail
        PersonEntity mockPerson = new PersonEntity();
        mockPerson.setEmail("testuser@example");
        mockPerson.setId(1L);
        mockPerson.setEnabled(true);
        mockPerson.setPassword("encodedPass");
        // добавьте роли, если нужно
        when(personRepository.findByEmail("testuser@example")).thenReturn(Optional.of(mockPerson));

        registrationRequest = new RegistrationCommand();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setEmail("testuser@gmail.com");
        registrationRequest.setPassword("password");
        registrationRequest.setBirthDate(LocalDate.of(1990, 1, 1));

        updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("JohnUpdated");
        updateRequest.setLastName("DoeUpdated");
        updateRequest.setGender("Male");
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        PersonDto personDto = new PersonDto("John",
                "Doe",
                "P-12345",
                null,
                false,
                null,
                null,
                null,
                true);
        when(personService.existsByEmail(any())).thenReturn(false);
        when(personService.register(any())).thenReturn(personDto);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User account created successfully"))
                .andExpect(jsonPath("$.data.firstName").value("John"));

        verify(personService, times(1)).register(any());
    }

    @Test
    void testRegisterUserEmailExists() throws Exception {
        loginRequest.setEmail("testuser@example.com");
        when(personService.existsByEmail(any())).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("AN ERROR HAS OCCURRED WHILE TRYING TO PROCESS YOUR REGISTRATION REQUEST. PLEASE CONTACT THE SERVICE DECK"))
                .andExpect(jsonPath("$.data").doesNotExist());


        verify(personService, never()).register(any());
    }

    @Test
    void testAuthenticateUserSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(
                new UPCUserDetails(1L,"A-12345", "testuser@example", "encodedPass", true,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );

        when(refreshTokenService.createRefreshToken(any())).thenReturn(new org.nikita.core.domain.RefreshToken());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Authentication Successful"))
                .andExpect(jsonPath("$.data.token").exists());

        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        // Создаём кастомный UPCUserDetails с нужными данными
        UPCUserDetails userDetails = new UPCUserDetails(
                1L,
                "A-12345",
                "test@example.com",
                "encodedPass",
                true,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Создаём Authentication с кастомным UserDetails
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        PersonDto updatedDto = new PersonDto("JohnUpdated",
                "DoeUpdated",
                "P-12345",
                null,
                false,
                null,
                null,
                null,
                true);
        when(personService.update(anyLong(), any())).thenReturn(updatedDto);

        mockMvc.perform(put("/api/auth/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth)))  // передаём кастомный Authentication
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.firstName").value("JohnUpdated"));

        verify(personService, times(1)).update(anyLong(), any());
    }

    @Test
    void testRefreshTokenSuccess() throws Exception {
        String refreshTokenValue = "refresh-token-sample";
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken(refreshTokenValue);

        org.nikita.core.domain.RefreshToken refreshToken = mock(org.nikita.core.domain.RefreshToken.class);
        PersonEntity user = new PersonEntity();
        user.setId(1L);
        user.setEmail("testuser@example");
        user.setPassword("encodedPass");
        user.setEnabled(true);
        user.setRoles(List.of()); // добавьте роли, если нужно

        // findByTokenOrThrow возвращает сам refreshToken
        when(refreshTokenService.findByTokenOrThrow(refreshTokenValue)).thenReturn(refreshToken);

        // validateRefreshTokenOrThrow - void метод, используйте doNothing()
        doNothing().when(refreshTokenService).validateRefreshTokenOrThrow(refreshToken);

        when(refreshToken.getUser()).thenReturn(user);

        mockMvc.perform(post("/api/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").value(refreshTokenValue));

        verify(refreshTokenService, times(1)).findByTokenOrThrow(refreshTokenValue);
        verify(refreshTokenService, times(1)).validateRefreshTokenOrThrow(refreshToken);
    }


    @Test
    void testRegisterUserInvalidEmail() throws Exception {
        registrationRequest.setEmail("not-an-email");
        when(personService.existsByEmail(any())).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("AN ERROR HAS OCCURRED WHILE TRYING TO PROCESS YOUR REGISTRATION REQUEST. PLEASE CONTACT THE SERVICE DECK"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(personService, never()).register(any());
    }

    @Test
    void testRegisterUserBlankFirstName() throws Exception {
        registrationRequest.setFirstName("");
        when(personService.existsByEmail(any())).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("AN ERROR HAS OCCURRED WHILE TRYING TO PROCESS YOUR REGISTRATION REQUEST. PLEASE CONTACT THE SERVICE DECK"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(personService, never()).register(any());
    }

    @Test
    void testAuthenticateUserInvalidPassword() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new LoginException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Oops!, no user found with : "))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

}
