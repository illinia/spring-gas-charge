package com.portfolio.gascharge.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

// JPA 리포지토리만 테스트하기 때문에 다른 빈들을 주입할 필요없이 엔티티, 리포지토리만 빈 생성해주는 @DataJpaTest를 사용하면 된다.
// @DataJpaTest 에서 @AutoConfigureTestDatabase 이 테스트할때 기본 DataSource를 사용해 메모리 DB를 사용하게 설정해준다
// 실제 DB를 사용하고 싶으면 replace = Relace.NONE 값을 넣어주면 된다.
@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void 회원생성() {
        //given
        String address1 = "address1";
        String address2 = "address2";
        String zipcode = "memory";
        Address address = new Address(address1, address2, zipcode);


        String name = "test";
        Member member = new Member(name, address);

        memberRepository.save(member);

        //when
        Long id = member.getId();

        Member findMember = memberRepository.findById(id).get();

        //then
        Assertions.assertThat(findMember.getName()).isEqualTo(name);
    }
}