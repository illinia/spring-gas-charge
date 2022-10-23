package com.portfolio.gascharge.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostReserveResponseDto {

    private String reservationId;
    private String userEmail;
    private String chargePlaceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reserveTime;
    private ReservationStatus status;

    @Builder
    public PostReserveResponseDto(String reservationId, String userEmail, String chargePlaceId, LocalDateTime reserveTime, ReservationStatus status) {
        this.reservationId = reservationId;
        this.userEmail = userEmail;
        this.chargePlaceId = chargePlaceId;
        this.reserveTime = reserveTime;
        this.status = status;
    }

    public static PostReserveResponseDto toResponseDto(Reservation reservation) {
        return PostReserveResponseDto.builder()
                .reservationId(reservation.getReservationValidationId())
                .userEmail(reservation.getUser().getEmail())
                .chargePlaceId(reservation.getCharge().getChargePlaceId())
                .reserveTime(reservation.getTime())
                .status(reservation.getStatus())
                .build();
    }
}
