package com.portfolio.gascharge.domain.member;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
//            테이블 매핑 컬럼명이 중복될시 사용
//    @AttributeOverrides({
//            @AttributeOverride(
//                    name = "address1",
//                    column = @Column(name = "address1"))
//    })
    private Address address;

    public Member(String name, String email, String picture, Role role, Address address) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.address = address;
    }

    public Member(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
