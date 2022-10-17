package com.portfolio.gascharge.controller.token;

import com.portfolio.gascharge.domain.token.Token;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final EntityManager em;

    @ApiOperation(
            value = "토근 값 조회", notes = "유저권한, 어드민권한 토큰 값 조회하는 컨트롤러입니다. 스웨거 페이지 상단에 Authorize 버튼을 눌러서 토큰값 입력하고 테스트하세요."
    )
    @GetMapping("/token")
    public ResponseEntity getTokens() {
        List<Token> tokens = em.createQuery("select t from Token t", Token.class)
                .getResultList();
        return new ResponseEntity(tokens, HttpStatus.OK);
    }
}
