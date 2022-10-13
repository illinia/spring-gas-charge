package com.portfolio.gascharge.repository.reservation;

import com.portfolio.gascharge.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.reservationValidationId = :reservationValidationId")
    Optional<Reservation> findByReservationValidationId(@Param("reservationValidationId") String id);
}
