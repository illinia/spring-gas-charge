package com.portfolio.gascharge.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForceUpdateReservationRequestDto {

    @ApiModelProperty(value = "예약 상태", example = "CANCEL")
    private ReservationStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "변경할 시간", example = "2022-10-13T20:00:00")
    private LocalDateTime time;
}
