package com.portfolio.gascharge.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReserveRequestDto {

    @ApiModelProperty(example = "1150020121HS2022025")
    @NotBlank
    private String chargePlaceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(example = "2022-10-13T20:00:00")
    @NotNull
    @Future
    private LocalDateTime time;
}
