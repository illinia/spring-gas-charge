package com.portfolio.gascharge.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    public void 회원생성() {
        //given
        String address1 = "address1";
        String address2 = "address2";
        String zipcode = "12345";
        Address address = new Address(address1, address2, zipcode);


        String name = "test";
        Member member = new Member(name, address);

        memberRepository.save(member);

        Long id = member.getId();

        //when
        Member findMember = memberRepository.findById(id).get();

        //then
        Assertions.assertThat(findMember.getName()).isEqualTo(name);
    }
}