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

    private String id;
    private String name;
    private Long totalCount;
    private Long currentCount;

    @Builder
    public SearchChargeResponseDto(String id, String name, Long totalCount, Long currentCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }

    public static SearchChargeResponseDto toResponseDto(Charge charge) {
        return SearchChargeResponseDto.builder()
                .id(charge.getId())
                .name(charge.getName())
                .totalCount(charge.getTotalCount())
                .currentCount(charge.getCurrentCount())
                .build();
    }
}
