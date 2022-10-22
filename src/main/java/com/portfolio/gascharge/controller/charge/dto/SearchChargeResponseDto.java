package com.portfolio.gascharge.controller.charge.dto;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchChargeResponseDto {

    private String chargePlaceId;
    private String name;
    private Long totalCount;
    private Long currentCount;
    private ChargePlaceMembership membership;

    @Builder
    public SearchChargeResponseDto(String chargePlaceId, String name, Long totalCount, Long currentCount, ChargePlaceMembership membership) {
        this.chargePlaceId = chargePlaceId;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
        this.membership = membership;
    }

    public static SearchChargeResponseDto toResponseDto(Charge charge) {
        return SearchChargeResponseDto.builder()
                .chargePlaceId(charge.getChargePlaceId())
                .name(charge.getName())
                .totalCount(charge.getTotalCount())
                .currentCount(charge.getCurrentCount())
                .membership(charge.getMembership())
                .build();
    }
}
