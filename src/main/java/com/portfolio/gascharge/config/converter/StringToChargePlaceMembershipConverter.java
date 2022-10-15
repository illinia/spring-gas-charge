package com.portfolio.gascharge.config.converter;

import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import org.springframework.core.convert.converter.Converter;

public class StringToChargePlaceMembershipConverter implements Converter<String, ChargePlaceMembership> {
    @Override
    public ChargePlaceMembership convert(String source) {
        return ChargePlaceMembership.getChargePlaceMembership(source);
    }
}
