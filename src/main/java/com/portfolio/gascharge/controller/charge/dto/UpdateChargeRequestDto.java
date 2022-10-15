package com.portfolio.gascharge.controller.charge.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateChargeRequestDto {

    @ApiModelProperty(value = "수정할 이름", example = "테스트 이름")
    private String name;

    @ApiModelProperty(value = "수정할 전체 충전 가능 수", example = "5")
    @NotNull
    @Min(value = 0)
    private Long totalCount;

    @ApiModelProperty(value = "수정할 현재 대기 수", example = "2")
    @NotNull
    @Min(value = 0)
    private Long currentCount;
}
