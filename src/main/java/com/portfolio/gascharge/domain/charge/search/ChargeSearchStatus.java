package com.portfolio.gascharge.domain.charge.search;

import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChargeSearchStatus {

    private String name;

    private ChargePlaceMembership chargePlaceMembership;

}
