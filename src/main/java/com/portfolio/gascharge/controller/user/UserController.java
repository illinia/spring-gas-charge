package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.controller.user.dto.GetUserCurrentResponse;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.error.exception.oauth2.ResourceNotFoundException;
import com.portfolio.gascharge.oauth.entity.CurrentUser;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.repository.user.UserRepository;
import com.portfolio.gascharge.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());

        GetUserCurrentResponse responseDto = GetUserCurrentResponse.toResponseDto(user);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
