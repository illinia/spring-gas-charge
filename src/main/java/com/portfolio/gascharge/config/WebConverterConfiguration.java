package com.portfolio.gascharge.config;

import com.portfolio.gascharge.config.converter.StringToAuthProviderConverter;
import com.portfolio.gascharge.config.converter.StringToChargePlaceMembershipConverter;
import com.portfolio.gascharge.config.converter.StringToUserAuthorityConverter;
import com.portfolio.gascharge.config.converter.StringToUserEmailVerifiedConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class WebConverterConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        var converters = new ArrayList<>(List.of(
                new StringToUserEmailVerifiedConverter(),
                new StringToAuthProviderConverter(),
                new StringToUserAuthorityConverter(),
                new StringToChargePlaceMembershipConverter()
        ));

        converters.forEach(registry::addConverter);
    }
}
