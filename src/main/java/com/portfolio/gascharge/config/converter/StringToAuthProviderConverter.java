package com.portfolio.gascharge.config.converter;

import com.portfolio.gascharge.oauth.entity.AuthProvider;
import org.springframework.core.convert.converter.Converter;

public class StringToAuthProviderConverter implements Converter<String, AuthProvider> {
    @Override
    public AuthProvider convert(String source) {
        return AuthProvider.valueOf(source.toLowerCase());
    }
}
