package com.portfolio.gascharge.batch.api;

import com.portfolio.gascharge.domain.charge.Charge;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChargeApiDto {

    private String id;
    private String name;
    private Long totalCount;
    private Long currentCount;

    @Builder
    public ChargeApiDto(String id, String name, Long totalCount, Long currentCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }

    public Charge toEntity() {
        return Charge.builder()
                .id(this.id)
                .name(this.name)
                .totalCount(this.totalCount)
                .currentCount(this.currentCount)
                .build();
    }
}
