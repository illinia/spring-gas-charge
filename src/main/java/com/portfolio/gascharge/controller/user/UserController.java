package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.oauth.entity.CurrentUser;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.oauth.exception.ResourceNotFoundException;
import com.portfolio.gascharge.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
