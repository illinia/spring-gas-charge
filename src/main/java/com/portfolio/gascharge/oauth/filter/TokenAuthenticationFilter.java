package com.portfolio.gascharge.oauth.filter;

import com.portfolio.gascharge.oauth.service.CustomUserDetailsService;
import com.portfolio.gascharge.oauth.token.AuthToken;
import com.portfolio.gascharge.oauth.token.AuthTokenProvider;
import com.portfolio.gascharge.utils.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenStr = HeaderUtil.getAccessToken(request);
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);

        if (token.validate()) {
            Authentication authentication = tokenProvider.getAuthentication(token);

//            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("doFilterInternal = {}", authentication.getPrincipal().toString());

            log.info("doFilterInternal service = {}",userDetailsService.loadUserByUsername(((User) authentication.getPrincipal()).getUsername()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(((User) authentication.getPrincipal()).getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
