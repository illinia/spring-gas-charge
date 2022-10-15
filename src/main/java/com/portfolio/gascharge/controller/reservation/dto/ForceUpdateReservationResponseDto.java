package com.portfolio.gascharge.controller.reservation.dto;

import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForceUpdateReservationResponseDto {

    private String reservationId;
    private String userEmail;
    private String chargePlaceId;
    private LocalDateTime reserveTime;
    private ReservationStatus status;

    @Builder
    public ForceUpdateReservationResponseDto(String reservationId, String userEmail, String chargePlaceId, LocalDateTime reserveTime, ReservationStatus status) {
        this.reservationId = reservationId;
        this.userEmail = userEmail;
        this.chargePlaceId = chargePlaceId;
        this.reserveTime = reserveTime;
        this.status = status;
    }

    public static ForceUpdateReservationResponseDto toResponseDto(Reservation reservation) {
        return ForceUpdateReservationResponseDto.builder()
                .reservationId(reservation.getReservationValidationId())
                .userEmail(reservation.getUser().getEmail())
                .chargePlaceId(reservation.getCharge().getChargePlaceId())
                .reserveTime(reservation.getTime())
                .status(reservation.getStatus())
                .build();
    }
}