package com.portfolio.gascharge.oauth.service;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import com.portfolio.gascharge.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    // 해당 메서드가 실행 안 되는 이유는 TokenAuthenticationFilter 에서 UsernamePasswordAuthenticationToken 객체를 안넣어줘서 그럼
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername() username = {}", username);

        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException("Can not find username");
        }

        log.info("loadUserByUsername user={}", user);
        return UserPrincipal.create(user);
    }


}
