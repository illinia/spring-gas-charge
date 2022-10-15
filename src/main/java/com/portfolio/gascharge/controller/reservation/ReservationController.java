package com.portfolio.gascharge.controller.reservation;

import com.portfolio.gascharge.controller.reservation.dto.*;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.oauth.entity.CurrentUser;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.service.reservation.ReservationService;
import com.portfolio.gascharge.utils.web.DtoFieldSpreader;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.portfolio.gascharge.domain.charge.ChargeTestData.CHARGE_TEST_ID;
import static com.portfolio.gascharge.domain.reservation.ReservationTestData.RESERVATION_TEST_UUID;
import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_EMAIL1;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @ApiOperation(
            value = "예약 생성", notes = "인증된 사용자가 ChargePlaceId 와 LocalDateTime 을 통해 예약을 생성한다."
    )
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity reserve(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid PostReserveRequestDto requestDto) {

        Reservation reserve = reservationService.reserve(userPrincipal.getId(), requestDto.getChargePlaceId(), requestDto.getTime());

        PostReserveResponseDto responseDto = PostReserveResponseDto.toResponseDto(reserve);

        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

    @ApiOperation(
            value = "본인 예약 수정", notes = "인증되었고 수정 요청한 예약의 이메일과 인증 사용자의 이메일이 같거나, ADMIN 역할일 경우 예약 시간을 수정한다."
    )
    @PreAuthorize("isAuthenticated() and ((#requestDto.email == principal.email) or hasRole('ADMIN'))")
    @PatchMapping("")
    public ResponseEntity updateSelfReservationTime(
            @RequestBody @Valid UpdateReservationRequestDto requestDto
            ) {
        Reservation reservation = reservationService.updateTime(requestDto.getReservationValidationId(), requestDto.getUpdateTime());

        return new ResponseEntity(UpdateReservationResponseDto.toResponseDto(reservation), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reservationValidationId}")
    public ResponseEntity forceUpdate(
            @PathVariable @NotBlank String reservationValidationId,
            @RequestBody ForceUpdateReservationRequestDto requestDto) {

        Map<String, Object> attributesMap = DtoFieldSpreader.of(requestDto);

        Reservation reservation = reservationService.updateDynamicField(reservationValidationId, attributesMap);

        return new ResponseEntity(ForceUpdateReservationResponseDto.toResponseDto(reservation), HttpStatus.OK);
    }

    @ApiOperation(
            value = "예약 단건 검색", notes = "reservationValidationId 로 예약 단건 검색한다."
    )
    @ApiImplicitParam(
            name = "reservationValidationId",
            value = "예약 식별 값",
            required = true,
            paramType = "path",
            defaultValue = RESERVATION_TEST_UUID
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{reservationValidationId}")
    public ResponseEntity getById(
            @PathVariable @NotBlank String reservationValidationId) {
        Reservation reservation = reservationService.findByReservationId(reservationValidationId);

        GetReservationResponseDto postReserveResponseDto = GetReservationResponseDto.toResponseDto(reservation);

        return new ResponseEntity(postReserveResponseDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = "예약 조회", notes = "전체 조회일 경우 인증 유저의 권한이 ADMIN 이어야 하고, 다건 조회일 경우 인증 유저의 이메일이 검색 조건의 이메일과 같아야 한다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "검색 조건 이메일, 어드민일 경우 생략가능(전체조회)",
                    defaultValue = USER_TEST_EMAIL1
            ),
            @ApiImplicitParam(
                    name = "chargePlaceId",
                    value = "충전소 식별 아이디",
                    defaultValue = CHARGE_TEST_ID
            )
    })
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and ((#email == principal.email) or hasRole('ADMIN'))")
    public ResponseEntity getList(
            @RequestParam(value = "email", required = false) @Email String email,
            @RequestParam(value = "chargePlaceId", required = false) String chargePlaceId,
            @NotNull Pageable pageable) {

        List<GetReservationResponseDto> collect = reservationService.findAll(email, chargePlaceId, pageable)
                .getContent().stream().map(GetReservationResponseDto::toResponseDto).collect(Collectors.toList());

        Page<GetReservationResponseDto> result = new PageImpl<>(collect, pageable, collect.size());

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(
            value = "예약 취소", notes = "인증된 사용자가 입력한 이메일과 같거나, 어드민 역할을 가지고 있을때 예약 상태를 취소로 변경합니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "본인 확인용 이메일",
                    defaultValue = USER_TEST_EMAIL1
            ),
            @ApiImplicitParam(
                    name = "reservationValidationId",
                    value = "충전소 식별 아이디",
                    defaultValue = CHARGE_TEST_ID
            )
    })
    @PostMapping("/cancel")
    @PreAuthorize("isAuthenticated() and ((#email == principal.email) or hasRole('ADMIN'))")
    public ResponseEntity cancel(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "email", required = false) @Email String email,
            @RequestParam(value = "reservationValidationId") @NotBlank String reservationValidationId) {

        boolean isHasAdminRole = userPrincipal.getAuthorities().stream().anyMatch(i -> i.getAuthority().equals(UserAuthority.ROLE_ADMIN.toString()));

        if (!isHasAdminRole) {
            // 이메일 동일 여부 체크 후 수정
            boolean isSameEmail = reservationService.checkSameEmail(email, reservationValidationId);

            if (!isSameEmail) {
                throw new AccessDeniedException(
                        "ReservationController cancelReservation 메서드에서 "
                                + userPrincipal.getEmail() + " " + userPrincipal.getAuthorities().toString() + " 역할의 유저가 예약 번호 "
                                + reservationValidationId + " 의 예약을 변경하려 시도하였으나 권한 없음으로 예외 처리 되었다.");
            }
        }

        Reservation reservation = reservationService.updateStatus(reservationValidationId, ReservationStatus.CANCEL);

        return new ResponseEntity(CancelReservationResponseDto.toResponseDto(reservation), HttpStatus.OK);
    }
}
