package com.portfolio.gascharge.service.reservation;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.ChargeTestData;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.reservation.search.ReservationSearchStatus;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.repository.reservation.ReservationRepository;
import com.portfolio.gascharge.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ChargeRepository chargeRepository;

    private User user;
    private User admin;
    private Charge charge;
    private LocalDateTime localDateTime;
    private Reservation reservation;

    @BeforeEach
    void set() {
        this.user = UserTestData.getCloneUser();
        this.admin = UserTestData.getCloneAdmin();
        this.charge = ChargeTestData.getTestCharge();
        this.localDateTime = LocalDateTime.now().plusHours(1);
        this.reservation = Reservation.builder()
                .reservationValidationId(UUID.randomUUID().toString())
                .user(this.user)
                .charge(this.charge)
                .time(this.localDateTime)
                .build();
    }

    @Test
    void reserve() {
        // given
        String email = this.user.getEmail();
        String chargePlaceId = this.charge.getChargePlaceId();
        LocalDateTime localDateTime = this.localDateTime;

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.reserve(email, chargePlaceId, localDateTime))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(this.user));
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.reserve(email, chargePlaceId, localDateTime))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(this.user));
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.of(this.charge));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(this.reservation);

        // then
        assertThat(reservationService.reserve(email, chargePlaceId, localDateTime)).isEqualTo(this.reservation);
    }

    @Test
    void updateStatus() {
        // given
        String reservationValidationId = this.reservation.getReservationValidationId();
        ReservationStatus status = ReservationStatus.NO_SHOW;

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.updateStatus(reservationValidationId, status))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.of(this.reservation));

        // then
        assertThat(reservationService.updateStatus(reservationValidationId, status).getStatus()).isEqualTo(status);
    }

    @Test
    void checkSameEmail() {
        // given
        String email = this.user.getEmail();
        String adminEmail = this.admin.getEmail();
        String reservationValidationId = this.reservation.getReservationValidationId();

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.checkSameEmail(email, reservationValidationId))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.of(this.reservation));

        // then
        assertThat(reservationService.checkSameEmail(email, reservationValidationId)).isTrue();
        assertThat(reservationService.checkSameEmail(adminEmail, reservationValidationId)).isFalse();
    }

    @Test
    void updateTime() {
        // given
        String reservationValidationId = this.reservation.getReservationValidationId();
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.updateTime(reservationValidationId, localDateTime))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.of(this.reservation));
        Reservation result = reservationService.updateTime(reservationValidationId, localDateTime);

        // then
        assertThat(result.getReservationValidationId()).isEqualTo(reservationValidationId);
        assertThat(result.getTime()).isEqualTo(localDateTime);
    }

    @Test
    void findByReservationId() {
        // given
        String reservationValidationId = this.reservation.getReservationValidationId();

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.findByReservationValidationId(reservationValidationId))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.of(this.reservation));
        Reservation result = reservationService.findByReservationValidationId(reservationValidationId);

        // then
        assertThat(result.getReservationValidationId()).isEqualTo(reservationValidationId);
        assertThat(result.getTime()).isEqualTo(this.localDateTime);
    }

    @Test
    void findAll() {
        // given
        Reservation reservation1 = Reservation.builder()
                .reservationValidationId(UUID.randomUUID().toString())
                .user(this.admin)
                .charge(this.charge)
                .time(LocalDateTime.now().plusDays(1))
                .build();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(this.reservation);
        reservationList.add(reservation1);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by("time").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Reservation> pageImpl = new PageImpl<>(reservationList, pageRequest, reservationList.size());

        // when
        when(reservationRepository.findReservationWithSearchStatus(any(ReservationSearchStatus.class), any(Pageable.class))).thenReturn(pageImpl);
        Page<Reservation> result = reservationService.findAll(null, null, pageRequest);

        // then
        assertThat(result.getPageable().getPageNumber()).isEqualTo(page);
        assertThat(result.getPageable().getPageSize()).isEqualTo(size);
        assertThat(result.getPageable().getSort()).isEqualTo(sort);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0)).isEqualTo(this.reservation);
        assertThat(result.getContent().get(1)).isEqualTo(reservation1);
    }

    @Test
    void updateDynamicField() {
        // given
        String reservationValidationId = this.reservation.getReservationValidationId();

        Map<String, Object> objectMap = new HashMap<>();
        ReservationStatus status = ReservationStatus.NO_SHOW;
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        objectMap.put("status", status);
        objectMap.put("time", localDateTime);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reservationService.updateDynamicField(reservationValidationId, null))
                .isInstanceOf(NoEntityFoundException.class);

        // when
        when(reservationRepository.findByReservationValidationId(anyString())).thenReturn(Optional.of(this.reservation));
        Reservation result = reservationService.updateDynamicField(reservationValidationId, objectMap);

        // then
        assertThat(result.getReservationValidationId()).isEqualTo(reservationValidationId);
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getTime()).isEqualTo(localDateTime);
    }






}