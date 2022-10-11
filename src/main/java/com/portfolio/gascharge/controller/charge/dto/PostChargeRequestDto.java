package com.portfolio.gascharge.controller.charge.dto;

import com.portfolio.gascharge.domain.charge.Charge;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostChargeRequestDto {

    @NotBlank(message = "chargePlaceId is mandatory")
    private String chargePlaceId;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotBlank(message = "totalCount is mandatory")
    private Long totalCount;
    @NotBlank(message = "currentCount is mandatory")
    private Long currentCount;

    public Charge toEntity() {
        return Charge.builder()
                .chargePlaceId(this.chargePlaceId)
                .name(this.name)
                .totalCount(this.totalCount)
                .currentCount(this.currentCount)
                .build();
    }
}
