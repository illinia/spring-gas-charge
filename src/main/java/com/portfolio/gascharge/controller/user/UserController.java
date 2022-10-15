package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.controller.user.dto.GetUserCurrentResponse;
import com.portfolio.gascharge.controller.user.dto.SearchUserResponseDto;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.search.UserSearchStatus;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.enums.user.UserEmailVerified;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import com.portfolio.gascharge.oauth.entity.CurrentUser;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.service.user.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_EMAIL1;
import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_NAME1;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @ApiOperation(
            value = "현재 인증된 유저 정보 검색", notes = "OAuth2 로 로그인한 인증된 유저 정보를 불러옵니다."
    )
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());

        return new ResponseEntity(GetUserCurrentResponse.toResponseDto(user), HttpStatus.OK);
    }

    @ApiOperation(
            value = "유저 전체, 다건 조회", notes = "이메일 포함, 이메일 인증 여부 일치, 이름 퐇마, 로그인 공급자 일치, 권한 일치 를 포함한 페이징 검색을 합니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "이메일 포함 검색",
                    defaultValue = USER_TEST_EMAIL1
            ),
            @ApiImplicitParam(
                    name = "email-verified",
                    value = "이메일 유효 상태 일치 검색",
                    defaultValue = "UNVERIFIED"
            ),
            @ApiImplicitParam(
                    name = "name",
                    value = "이름 포함 검색",
                    defaultValue = USER_TEST_NAME1
            ),
            @ApiImplicitParam(
                    name = "provider",
                    value = "로그인 공급자 이름 일치 검색",
                    defaultValue = "google"
            ),
            @ApiImplicitParam(
                    name = "user-authority",
                    value = "인증 권한 일치 검색",
                    defaultValue = "ROLE_USER"
            )

    })
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getUserList(
            @RequestParam(value = "email", required = false) @Email String email,
            @RequestParam(value = "email-verified", required = false) UserEmailVerified emailVerified,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "provider", required = false) AuthProvider provider,
            @RequestParam(value = "user-authority", required = false) UserAuthority userAuthority,
            @NotNull Pageable pageable) {

        UserSearchStatus userSearchStatus = new UserSearchStatus();
        userSearchStatus.setEmail(email);
        userSearchStatus.setEmailVerified(emailVerified);
        userSearchStatus.setName(name);
        userSearchStatus.setAuthProvider(provider);
        userSearchStatus.setUserAuthority(userAuthority);

        List<SearchUserResponseDto> collect = userService.findAll(userSearchStatus, pageable)
                .getContent().stream().map(SearchUserResponseDto::toResponseDto).collect(Collectors.toList());

        PageImpl<SearchUserResponseDto> result = new PageImpl<>(collect, pageable, collect.size());

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
