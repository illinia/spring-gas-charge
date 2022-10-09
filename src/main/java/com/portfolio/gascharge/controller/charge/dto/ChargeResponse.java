package com.portfolio.gascharge.controller.charge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChargeResponse {

    private String id;
    private String name;
    private Long totalCount;
    private Long currentCount;

    @Builder
    public ChargeResponse(String id, String name, Long totalCount, Long currentCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
        this.currentCount = currentCount;
    }
}
