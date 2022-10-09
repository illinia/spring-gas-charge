package com.portfolio.gascharge.enums.charge;

public enum ChargePlaceMembership {
    MEMBERSHIP, NOT_MEMBERSHIP;

    public static ChargePlaceMembership getChargePlaceMembership(String name) {
        if (name == null) {
            return null;
        }
        if (name.equals("membership")) {
            return ChargePlaceMembership.MEMBERSHIP;
        }
        if (name.equals("not-membership")) {
            return ChargePlaceMembership.NOT_MEMBERSHIP;
        }

        throw new RuntimeException("No matched membership name");
    }
}
