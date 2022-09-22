package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.common.ApiResponse;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.oauth.entity.CurrentUser;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse getUser(@CurrentUser UserPrincipal userPrincipal) {

        log.info("SecurityContextHolder = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        log.info("getUser username = {}", userPrincipal.getUsername());

        User user = userService.getUser(userPrincipal.getUsername());

        log.info("getUser user = {}", user);

        return ApiResponse.success("user", user);
    }
}
