package com.portfolio.gascharge.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.portfolio.gascharge.config.swagger.alternateType.PageableAlternativeRules;
import com.portfolio.gascharge.config.swagger.alternateType.UserPrincipalAlternativeRules;
import com.portfolio.gascharge.oauth.entity.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("수소 충전소 현황 조회/예약 시스템")
                .description("전 회사에서 앱 프론트 개발자로서 실제로 개발하고 배포중인 앱에서 백엔드 부분을 구현한 프로젝트입니다.")
                .build();
    }
    @Bean
    public Docket commonApi() {
        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(PageableAlternativeRules.class))
                        ,AlternateTypeRules.newRule(typeResolver.resolve(UserPrincipal.class), typeResolver.resolve(UserPrincipalAlternativeRules.class))
                )
                .useDefaultResponseMessages(false)
                .apiInfo(this.apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .groupName("taemin")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.portfolio.gascharge.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
