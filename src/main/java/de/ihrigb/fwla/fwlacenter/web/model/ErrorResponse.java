package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse<T> {
    private final String message;
    private final Optional<T> entity;
}
