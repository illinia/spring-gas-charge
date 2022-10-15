package com.portfolio.gascharge.domain.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.gascharge.domain.BaseTimeEntity;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString(exclude = {"user", "charge"})
@DynamicInsert
@Entity
public class Reservation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    @JsonIgnore
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String reservationValidationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "CHARGE_ID", nullable = false)
    private Charge charge;

    @Column(nullable = false)
    private LocalDateTime time;

    @ColumnDefault(value = "'BEFORE_CHARGE'")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Builder
    public Reservation(User user, Charge charge, LocalDateTime time, String reservationValidationId) {
        this.reservationValidationId = Objects.isNull(reservationValidationId) ? UUID.randomUUID().toString() : reservationValidationId;
        this.user = user;
        this.charge = charge;
        this.time = time;
        this.status = ReservationStatus.BEFORE_CHARGE;
    }

    public void removeUser() {
        if (!Objects.isNull(this.user)) {
            this.user.getReservations().remove(this);
        }
        this.user = null;
    }

    public void changeUser(User user) {
        if (!Objects.isNull(this.user)) {
            this.user.getReservations().remove(this);
        }
        this.user = user;
        user.getReservations().add(this);
    }

    public void removeCharge() {
        if (!Objects.isNull(this.charge)) {
            this.charge.getReservations().remove(this);
        }
        this.charge = null;
    }

    public void changeCharge(Charge charge) {
        if (!Objects.isNull(this.charge)) {
            this.charge.getReservations().remove(this);
        }
        this.charge = charge;
        charge.getReservations().add(this);
    }

    public ReservationStatus updateStatus(ReservationStatus status) {
        this.status = status;
        return this.status;
    }

    public LocalDateTime updateTime(LocalDateTime time) {
        this.time = time;
        return this.time;
    }
}
