package com.portfolio.gascharge.controller.reservation.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.portfolio.gascharge.domain.reservation.ReservationTestData.RESERVATION_TEST_UUID;
import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_EMAIL1;

@Data
public class UpdateReservationRequestDto {

    @ApiModelProperty(value = "예약 식별 아이디", example = RESERVATION_TEST_UUID)
    @NotBlank
    private String reservationValidationId;

    @ApiModelProperty(value = "유저 이메일 해당 유저가 예약할 유저와 일치하는지 이메일로 비교하기 위한 값", example = USER_TEST_EMAIL1)
    @Email
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "변경할 시간", example = "2022-10-13T20:00:00")
    @NotNull
    @Future
    private LocalDateTime updateTime;
}
