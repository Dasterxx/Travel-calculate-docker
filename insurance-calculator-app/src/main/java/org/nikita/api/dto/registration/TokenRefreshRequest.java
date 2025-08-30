package org.nikita.api.dto.registration;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
