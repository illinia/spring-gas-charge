package com.portfolio.gascharge.domain.charge;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.gascharge.domain.BaseTimeEntity;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@ToString(exclude = {"reservations"})
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"CHARGE_ID", "chargePlaceId"})
})
@DynamicInsert
public class Charge extends BaseTimeEntity implements Cloneable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHARGE_ID")
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String chargePlaceId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long totalCount;

    @Column(nullable = false)
    private Long currentCount;

    @Setter
    @ColumnDefault(value = "'NOT_MEMBERSHIP'")
    @Enumerated(EnumType.STRING)
    private ChargePlaceMembership membership;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "charge")
    @JsonBackReference
    private List<Reservation> reservations = new ArrayList<>();

    @Builder
    public Charge(String chargePlaceId, String name, Long totalCount, Long currentCount, ChargePlaceMembership membership) {
        this.chargePlaceId = chargePlaceId;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
        this.membership = membership;
    }

    public void updateCounts(Long totalCount, Long currentCount) {
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }

    @Override
    public Charge clone() {
        try {
            return (Charge) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
