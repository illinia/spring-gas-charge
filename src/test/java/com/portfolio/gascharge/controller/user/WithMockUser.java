package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.enums.user.UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockUser {
    UserAuthority authority() default UserAuthority.ROLE_USER;
}
