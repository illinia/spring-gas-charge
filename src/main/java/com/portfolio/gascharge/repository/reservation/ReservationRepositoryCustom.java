package com.portfolio.gascharge.repository.reservation;

import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.reservation.search.ReservationSearchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {
    Page<Reservation> findReservationWithSearchStatus(ReservationSearchStatus reservationSearchStatus, Pageable pageable);
}
