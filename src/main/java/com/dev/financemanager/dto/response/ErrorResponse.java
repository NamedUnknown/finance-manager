package com.dev.financemanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private final Integer status;
    private final String message;
}
