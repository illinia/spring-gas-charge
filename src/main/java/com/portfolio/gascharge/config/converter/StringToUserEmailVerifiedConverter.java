package com.portfolio.gascharge.config.converter;

import com.portfolio.gascharge.enums.user.UserEmailVerified;
import org.springframework.core.convert.converter.Converter;

public class StringToUserEmailVerifiedConverter implements Converter<String, UserEmailVerified> {
    @Override
    public UserEmailVerified convert(String source) {
        return UserEmailVerified.valueOf(source.toUpperCase());
    }
}
