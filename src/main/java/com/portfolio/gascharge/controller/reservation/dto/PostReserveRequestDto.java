package com.portfolio.gascharge.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostReserveRequestDto {

    @ApiModelProperty(value = "충전소 식별 아이디", example = "1150020121HS2022025")
    @NotBlank
    private String chargePlaceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "예약 시간", example = "2022-10-13T20:00:00")
    @NotNull
    @Future
    private LocalDateTime time;

    @Builder
    public PostReserveRequestDto(String chargePlaceId, LocalDateTime time) {
        this.chargePlaceId = chargePlaceId;
        this.time = time;
    }
}
