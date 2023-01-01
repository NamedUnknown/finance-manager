package com.dev.financemanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SuccessfulResponse<T> {
    private final Integer status;
    private final String message;
    private final T object;
}
