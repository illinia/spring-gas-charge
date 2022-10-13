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

    @ApiModelProperty(example = RESERVATION_TEST_UUID)
    @NotBlank
    private String reservationValidationId;

    @ApiModelProperty(example = USER_TEST_EMAIL1)
    @Email
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(example = "2022-10-13T20:00:00")
    @NotNull
    @Future
    private LocalDateTime updateTime;
}
