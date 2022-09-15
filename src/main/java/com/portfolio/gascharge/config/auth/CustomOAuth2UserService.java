package com.portfolio.gascharge.config.auth;

import com.portfolio.gascharge.config.auth.dto.OAuthAttributes;
import com.portfolio.gascharge.domain.member.Member;
import com.portfolio.gascharge.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * 현재 진행중인 서비스를 구분하는 코드
         * 구글만 사용하지만 네이버 연동시에 구분하기 위해 사용
         */
        String registrationId = userRequest
                .getClientRegistration().getRegistrationId();

        /**
         * OAuth2 로그인 진행시 키가 되는 필드값. Primary Key 와 같은 의미
         * 구글은 지원, 네이버나 카카오는 미지원
         * 향우 네이버, 구글 로그인을 동시 지원할 때 사용
         */
        String userNameAttributeName = userRequest
                .getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(
                        attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        System.out.println(member);
        return memberRepository.save(member);
    }
}
