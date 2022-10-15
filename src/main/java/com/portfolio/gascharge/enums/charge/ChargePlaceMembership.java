package com.portfolio.gascharge.enums.charge;

import com.portfolio.gascharge.error.exception.enums.NotFoundMatchedEnum;

import java.util.Arrays;

public enum ChargePlaceMembership {
    MEMBERSHIP("membership"),
    NOT_MEMBERSHIP("not-membership");

    private String membershipString;

    ChargePlaceMembership(String membershipString) {
        this.membershipString = membershipString;
    }

    public static ChargePlaceMembership getChargePlaceMembership(String name) {
        return Arrays.stream(ChargePlaceMembership.values())
                .filter(m -> m.membershipString.equals(name))
                .findAny()
                .orElseThrow(() -> new NotFoundMatchedEnum(ChargePlaceMembership.class, name));
    }
}
