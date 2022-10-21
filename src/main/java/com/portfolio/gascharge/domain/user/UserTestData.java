package com.portfolio.gascharge.domain.user;

import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.enums.user.UserEmailVerified;
import com.portfolio.gascharge.oauth.entity.AuthProvider;

public class UserTestData {
    public static final String USER_TEST_NAME = "테스트";
    public static final String USER_TEST_EMAIL = "test@test.com";
    public static final String USER_TEST_IMAGE_URL = "testImageUrl";
    public static final UserEmailVerified USER_TEST_EMAIL_VERIFIED = UserEmailVerified.UNVERIFIED;
    public static final String USER_TEST_PASSWORD = "testPassword";
    public static final AuthProvider USER_TEST_AUTH_PROVIDER = AuthProvider.google;
    public static final String USER_TEST_PROVIDER_ID = "testProviderId";
    public static final UserAuthority USER_TEST_USER_AUTHORITY = UserAuthority.ROLE_USER;

    public static final User USER_TEST = User.builder()
            .name(USER_TEST_NAME)
            .email(USER_TEST_EMAIL)
            .imageUrl(USER_TEST_IMAGE_URL)
            .emailVerified(USER_TEST_EMAIL_VERIFIED)
            .password(USER_TEST_PASSWORD)
            .provider(USER_TEST_AUTH_PROVIDER)
            .providerId(USER_TEST_PROVIDER_ID)
            .userAuthority(USER_TEST_USER_AUTHORITY)
            .build();

    public static final String ADMIN_TEST_NAME = "어드민";
    public static final String ADMIN_TEST_EMAIL = "admin@test.com";
    public static final String ADMIN_TEST_IMAGE_URL = "testAdminImageUrl";
    public static final UserEmailVerified ADMIN_TEST_EMAIL_VERIFIED = UserEmailVerified.VERIFIED;
    public static final String ADMIN_TEST_PASSWORD = "testAdminPassword";
    public static final AuthProvider ADMIN_TEST_AUTH_PROVIDER = AuthProvider.kakao;
    public static final String ADMIN_TEST_PROVIDER_ID = "testAdminProviderId";
    public static final UserAuthority ADMIN_TEST_USER_AUTHORITY = UserAuthority.ROLE_ADMIN;

    public static final User ADMIN_TEST = User.builder()
            .name(ADMIN_TEST_NAME)
            .email(ADMIN_TEST_EMAIL)
            .imageUrl(ADMIN_TEST_IMAGE_URL)
            .emailVerified(ADMIN_TEST_EMAIL_VERIFIED)
            .password(ADMIN_TEST_PASSWORD)
            .provider(ADMIN_TEST_AUTH_PROVIDER)
            .providerId(ADMIN_TEST_PROVIDER_ID)
            .userAuthority(ADMIN_TEST_USER_AUTHORITY)
            .build();
}
