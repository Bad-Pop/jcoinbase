package com.github.badpop.jcoinbase.client.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private final String id;
    private final String message;
    private final String url;
}
