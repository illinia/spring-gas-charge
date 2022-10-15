package com.portfolio.gascharge.controller.charge.dto;

import com.portfolio.gascharge.domain.charge.Charge;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddChargeRequestDto {

    @NotBlank
    @ApiModelProperty(example = "testId")
    private String chargePlaceId;
    @NotBlank
    @ApiModelProperty(example = "testName")
    private String name;
    @ApiModelProperty(example = "5")
    @NotNull
    @Min(value = 0)
    private Long totalCount;
    @ApiModelProperty(example = "2")
    @NotNull
    @Min(value = 0)
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
