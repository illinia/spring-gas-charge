package com.portfolio.gascharge.config.converter;

import com.portfolio.gascharge.enums.user.UserAuthority;
import org.springframework.core.convert.converter.Converter;

public class StringToUserAuthorityConverter implements Converter<String, UserAuthority> {
    @Override
    public UserAuthority convert(String source) {
        return UserAuthority.valueOf(source.toUpperCase());
    }
}
