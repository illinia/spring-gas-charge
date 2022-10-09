package com.portfolio.gascharge.domain.charge;

import com.portfolio.gascharge.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
@ToString
public class Charge extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long totalCount;

    @Column(nullable = false)
    private Long currentCount;

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
