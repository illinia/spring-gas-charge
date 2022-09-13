package com.portfolio.gascharge.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
//            테이블 매핑 컬럼명이 중복될시 사용
//    @AttributeOverrides({
//            @AttributeOverride(
//                    name = "address1",
//                    column = @Column(name = "address1"))
//    })
    private Address address;

    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
