package com.portfolio.gascharge.domain.charge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Charge {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer currentCount;

    @Builder
    public Charge(String id, String name, Integer totalCount, Integer currentCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }
}
