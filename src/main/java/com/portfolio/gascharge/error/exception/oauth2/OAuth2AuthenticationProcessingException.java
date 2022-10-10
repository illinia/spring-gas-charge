package com.portfolio.gascharge.error.exception.oauth2;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
