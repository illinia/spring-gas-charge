package com.portfolio.gascharge.controller.user;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PrincipalDetailsArgumentResolver implements HandlerMethodArgumentResolver {

    private final User user;

    public PrincipalDetailsArgumentResolver(User user) {
        this.user = user;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserPrincipal.create(user);
    }
}
