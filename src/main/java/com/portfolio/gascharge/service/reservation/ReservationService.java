package com.portfolio.gascharge.service.reservation;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import com.portfolio.gascharge.error.errorcode.CommonErrorCode;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.error.exception.web.RestApiException;
import com.portfolio.gascharge.repository.reservation.ReservationRepository;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;
    private final ReservationRepository reservationRepository;

    public Reservation reserve(Long userId, String chargePlaceId, LocalDateTime time) {
        Optional<User> byUserId = userRepository.findById(userId);

        if (byUserId.isEmpty()) {
            throw new NoEntityFoundException(User.class, userId.toString());
        }

        Optional<Charge> byChargeId = chargeRepository.findByChargePlaceId(chargePlaceId);

        if (byChargeId.isEmpty()) {
            throw new NoEntityFoundException(Charge.class, chargePlaceId);
        }

        User user = byUserId.get();
        Charge charge = byChargeId.get();

        Reservation reservation = Reservation.builder()
                .user(user)
                .charge(charge)
                .time(time)
                .build();

        Reservation save = reservationRepository.save(reservation);

        return save;
    }

    public ReservationStatus updateStatus(Long reservationId, ReservationStatus status) {
        Optional<Reservation> byId = reservationRepository.findById(reservationId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, reservationId.toString());
        }

        Reservation reservation = byId.get();

        return reservation.updateStatus(status);
    }

    public Reservation updateTime(String reservationValidationId, LocalDateTime time) {
        if (!time.getClass().equals(LocalDateTime.class)) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER, "LocalDateTime 파라미터가 적합하지 않습니다. 값 = " + time.toString());
        }

        Optional<Reservation> byId = reservationRepository.findByReservationValidationId(reservationValidationId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, reservationValidationId);
        }

        Reservation reservation = byId.get();

        reservation.updateTime(time);

        return reservation;
    }
}
