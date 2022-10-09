package com.portfolio.gascharge.domain.charge;

import com.portfolio.gascharge.domain.BaseTimeEntity;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@ToString
@DynamicInsert
public class Charge extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long totalCount;

    @Column(nullable = false)
    private Long currentCount;

    @Setter
    @Column(name = "membership")
    @ColumnDefault(value = "'NOT_MEMBERSHIP'")
    @Enumerated(EnumType.STRING)
    private ChargePlaceMembership membership;

    @Builder
    public Charge(String id, String name, Long totalCount, Long currentCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }

    public void update(Long totalCount, Long currentCount) {
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }
}
