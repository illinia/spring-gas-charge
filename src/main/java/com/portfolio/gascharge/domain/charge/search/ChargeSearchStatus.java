package com.portfolio.gascharge.domain.charge.search;

import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class ChargeSearchStatus {

    private String name;

    @Enumerated(EnumType.STRING)
    private ChargePlaceMembership chargePlaceMembership;

}
