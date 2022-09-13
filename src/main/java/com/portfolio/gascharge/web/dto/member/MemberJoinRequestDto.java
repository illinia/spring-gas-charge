package com.portfolio.gascharge.web.dto.member;

import com.portfolio.gascharge.domain.member.Address;
import com.portfolio.gascharge.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequestDto {
    private String name;
    private String address1;
    private String address2;
    private String zipcode;

    public Member toEntity() {
        Address address = new Address(address1, address2, zipcode);
        return new Member(name, address);
    }
}
