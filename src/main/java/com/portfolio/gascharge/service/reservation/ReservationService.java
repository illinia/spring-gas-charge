package com.portfolio.gascharge.service.reservation;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.reservation.search.ReservationSearchStatus;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.repository.reservation.ReservationRepository;
import com.portfolio.gascharge.repository.user.UserRepository;
import com.portfolio.gascharge.utils.entity.EntityDynamicUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
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

    public Reservation updateStatus(String reservationValidationId, ReservationStatus status) {
        Optional<Reservation> byId = reservationRepository.findByReservationValidationId(reservationValidationId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, reservationValidationId);
        }

        Reservation reservation = byId.get();

        reservation.updateStatus(status);

        return reservation;
    }

    @Transactional(readOnly = true)
    public boolean checkSameEmail(String email, String reservationValidationId) {
        Optional<Reservation> byId = reservationRepository.findByReservationValidationId(reservationValidationId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, reservationValidationId);
        }

        Reservation reservation = byId.get();

        return email.equals(reservation.getUser().getEmail());
    }

    public Reservation updateTime(String reservationValidationId, LocalDateTime time) {
        Optional<Reservation> byId = reservationRepository.findByReservationValidationId(reservationValidationId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, reservationValidationId);
        }

        Reservation reservation = byId.get();

        reservation.updateTime(time);

        return reservation;
    }

    @Transactional(readOnly = true)
    public Reservation findByReservationId(String id) {
        Optional<Reservation> byId = reservationRepository.findByReservationValidationId(id);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, id);
        }

        return byId.get();
    }

    @Transactional(readOnly = true)
    public Page<Reservation> findAll(String email, String chargePlaceId, Pageable pageable) {
        ReservationSearchStatus reservationSearchStatus = new ReservationSearchStatus();
        reservationSearchStatus.setEmail(email);
        reservationSearchStatus.setChargePlaceId(chargePlaceId);

        return reservationRepository.findReservationWithSearchStatus(reservationSearchStatus, pageable);
    }

    public Reservation updateDynamicField(String reservationValidationId, Map<String, Object> attributesMap) {
        Optional<Reservation> byId = reservationRepository.findByReservationValidationId(reservationValidationId);

        if (byId.isEmpty()) {
            throw new NoEntityFoundException(Reservation.class, reservationValidationId);
        }

        Reservation reservation = byId.get();

        EntityDynamicUpdater.update(attributesMap, reservation);

        return reservation;
    }
}
