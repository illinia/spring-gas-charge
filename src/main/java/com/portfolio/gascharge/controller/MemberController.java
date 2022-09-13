package com.portfolio.gascharge.controller;

import com.portfolio.gascharge.service.member.MemberService;
import com.portfolio.gascharge.web.dto.member.MemberJoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    /**
     * 컨트롤러에서 요청 바디 파라미터를 받을때 요청 전용 Dto 제작
     * 서비스 계층은 Dto 로 부터 독립적이게 하기위해
     * 서비스 계층에 넘길때는 엔티티로 변경해서 사용
     */
    public Long join(@RequestBody MemberJoinRequestDto requestDto) {
        return memberService.memberJoin(requestDto.toEntity());
    }
}
