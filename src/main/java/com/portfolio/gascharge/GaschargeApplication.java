package com.portfolio.gascharge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import com.portfolio.gascharge.oauth.token.TokenProvider;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.repository.reservation.ReservationRepository;
import com.portfolio.gascharge.repository.user.UserRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static com.portfolio.gascharge.domain.reservation.ReservationTestData.RESERVATION_TEST_UUID;
import static com.portfolio.gascharge.domain.user.UserTestData.*;

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaAuditing
public class GaschargeApplication {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private ChargeRepository chargeRepository;

	public static void main(String[] args) {
		SpringApplication.run(GaschargeApplication.class, args);
	}

	@PostConstruct
	@Transactional
	public void initTestValue() {
		User user = User.builder()
				.name(USER_TEST_NAME1)
				.email(USER_TEST_EMAIL1)
				.emailVerified(true)
				.provider(AuthProvider.google)
				.build();
		userRepository.save(user);

		User admin = User.builder()
				.name(USER_TEST_NAME2)
				.email(USER_TEST_EMAIL2)
				.emailVerified(true)
				.provider(AuthProvider.google)
				.userAuthority(UserAuthority.ROLE_ADMIN)
				.build();
		userRepository.save(admin);

		String token = tokenProvider.jwtTokenBuild(user.getId().toString(), tokenProvider.getLongExpiryDay(), "taemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemin");
		String tokenAdmin = tokenProvider.jwtTokenBuild(admin.getId().toString(), tokenProvider.getLongExpiryDay(), "taemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemin");

		System.out.println("Test id token = Bearer " + token);
		System.out.println("Admin test id token = Bearer " + tokenAdmin);

		Charge charge = Charge.builder()
				.chargePlaceId("testId")
				.name("test")
				.totalCount(5L)
				.currentCount(2L)
				.build();

		Charge charge1 = chargeRepository.save(charge);

		Reservation reservation = Reservation.builder()
				.time(LocalDateTime.now())
				.charge(charge1)
				.user(user)
				.reservationValidationId(RESERVATION_TEST_UUID)
				.build();

		Reservation save = reservationRepository.save(reservation);

		String reservationValidationId = save.getReservationValidationId();

		System.out.println("Test reservation user email = " + user.getEmail());
		System.out.println("Test reservationValidationId = " + reservationValidationId);
	}

}
