package com.portfolio.gascharge.repository.reservation;

import com.portfolio.gascharge.domain.reservation.QReservation;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.reservation.search.ReservationSearchStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.portfolio.gascharge.domain.reservation.QReservation.reservation;
import static com.portfolio.gascharge.utils.querydsl.QueryDslUtil.getOrderSpecifiers;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Reservation> findReservationWithSearchStatus(ReservationSearchStatus reservationSearchStatus, Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = getOrderSpecifiers(pageable, Reservation.class, "reservation");

        List<Reservation> fetch = queryFactory
                .selectFrom(reservation)
                .where(
                        reservationEmailLike(reservationSearchStatus.getEmail()),
                        reservationChargePlaceIdEq(reservationSearchStatus.getChargePlaceId())
                )
                .orderBy(orderSpecifiers.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int size = fetch.size();

        return new PageImpl<>(fetch, pageable, size);
    }

    private BooleanExpression reservationEmailLike(String email) {
        return StringUtils.isNullOrEmpty(email) ? null : reservation.user.email.eq(email);
    }

    private BooleanExpression reservationChargePlaceIdEq(String id) {
        return StringUtils.isNullOrEmpty(id) ? null : reservation.charge.chargePlaceId.eq(id);
    }
}
