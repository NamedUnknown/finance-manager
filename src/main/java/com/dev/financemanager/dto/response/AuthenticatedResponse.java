package com.dev.financemanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticatedResponse {
    private final Integer status;
    private final String message;
    private final String jwtToken;

}
