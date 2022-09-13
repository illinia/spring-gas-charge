package com.portfolio.gascharge.service.member;

import com.portfolio.gascharge.domain.member.Member;
import com.portfolio.gascharge.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service // 검색
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long memberJoin(Member member) {
        // 유효성 검사 로직 필요
        return memberRepository.save(member).getId();
    }
}
