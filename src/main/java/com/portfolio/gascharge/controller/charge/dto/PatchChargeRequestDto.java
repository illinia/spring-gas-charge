package com.portfolio.gascharge.controller.charge.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PatchChargeRequestDto {

    @NotBlank(message = "chargePlaceId is mandatory")
    private String chargePlaceId;
    private String name;
    private Long totalCount;
    private Long currentCount;
}
