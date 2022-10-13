package com.portfolio.gascharge.controller.reservation;

import com.portfolio.gascharge.controller.reservation.dto.ReserveRequestDto;
import com.portfolio.gascharge.controller.reservation.dto.ReserveResponseDto;
import com.portfolio.gascharge.controller.reservation.dto.UpdateReservationRequestDto;
import com.portfolio.gascharge.controller.reservation.dto.UpdateReservationResponseDto;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.oauth.entity.CurrentUser;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.service.reservation.ReservationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @ApiOperation(
            value = "예약 생성", notes = "로그인한 상태에서 ChargePlaceId 와 LocalDateTime 을 통해 예약을 생성한다."
    )
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity reserve(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ReserveRequestDto requestDto) {

        Reservation reserve = reservationService.reserve(userPrincipal.getId(), requestDto.getChargePlaceId(), requestDto.getTime());

        ReserveResponseDto responseDto = ReserveResponseDto.toResponseDto(reserve);

        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("isAuthenticated() and (#requestDto.email == principal.email)")
    public ResponseEntity updateReservationTime(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid UpdateReservationRequestDto requestDto
            ) {
        Reservation reservation = reservationService.updateTime(requestDto.getReservationValidationId(), requestDto.getUpdateTime());

        return new ResponseEntity(UpdateReservationResponseDto.toResponseDto(reservation), HttpStatus.OK);
    }
}
