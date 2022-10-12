package com.portfolio.gascharge.controller.charge.dto;

import com.portfolio.gascharge.domain.charge.Charge;
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

    @Builder
    public SearchChargeResponseDto(String chargePlaceId, String name, Long totalCount, Long currentCount) {
        this.chargePlaceId = chargePlaceId;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }

    public static SearchChargeResponseDto toResponseDto(Charge charge) {
        return SearchChargeResponseDto.builder()
                .chargePlaceId(charge.getChargePlaceId())
                .name(charge.getName())
                .totalCount(charge.getTotalCount())
                .currentCount(charge.getCurrentCount())
                .build();
    }
}
