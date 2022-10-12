package com.portfolio.gascharge;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import com.portfolio.gascharge.oauth.token.TokenProvider;
import com.portfolio.gascharge.repository.user.UserRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaAuditing
public class GaschargeApplication {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenProvider tokenProvider;

	public static void main(String[] args) {
		SpringApplication.run(GaschargeApplication.class, args);
	}

	@PostConstruct
	@Transactional
	public void initTestValue() {
		User user = User.builder()
				.name("테스트")
				.email("test@test.com")
				.emailVerified(true)
				.provider(AuthProvider.google)
				.build();
		userRepository.save(user);

		User admin = User.builder()
				.name("어드민")
				.email("admin@test.com")
				.emailVerified(true)
				.provider(AuthProvider.google)
				.userAuthority(UserAuthority.ROLE_ADMIN)
				.build();
		userRepository.save(admin);

		String token = tokenProvider.jwtTokenBuild(user.getId().toString(), tokenProvider.getLongExpiryDay(), "taemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemin");
		String tokenAdmin = tokenProvider.jwtTokenBuild(admin.getId().toString(), tokenProvider.getLongExpiryDay(), "taemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemin");

		System.out.println("Test id token = Bearer " + token);
		System.out.println("Admin test id token = Bearer " + tokenAdmin);
	}

}
