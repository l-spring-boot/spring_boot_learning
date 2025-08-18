package com.ig.spring_boot_learning.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiErrorException extends RuntimeException {
    private int code;
    private String message;


    @Override
    public String getLocalizedMessage() {
        return message;
    }
}
