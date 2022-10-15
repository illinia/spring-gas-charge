package com.portfolio.gascharge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.enums.user.UserEmailVerified;
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

import static com.portfolio.gascharge.domain.charge.ChargeTestData.*;
import static com.portfolio.gascharge.domain.reservation.ReservationTestData.*;
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
				.provider(AuthProvider.google)
				.build();
		userRepository.save(user);

		User admin = User.builder()
				.name(USER_TEST_NAME2)
				.email(USER_TEST_EMAIL2)
				.emailVerified(UserEmailVerified.VERIFIED)
				.provider(AuthProvider.google)
				.userAuthority(UserAuthority.ROLE_ADMIN)
				.build();
		userRepository.save(admin);

		String token = tokenProvider.jwtTokenBuild(user.getId().toString(), tokenProvider.getLongExpiryDay(), "taemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemin");
		String tokenAdmin = tokenProvider.jwtTokenBuild(admin.getId().toString(), tokenProvider.getLongExpiryDay(), "taemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemintaemin");

		System.out.println("Test id token = Bearer " + token);
		System.out.println("Admin test id token = Bearer " + tokenAdmin);

		Charge charge = Charge.builder()
				.chargePlaceId(CHARGE_TEST_ID)
				.name(CHARGE_TEST_NAME)
				.totalCount(CHARGE_TEST_TOTAL_COUNT)
				.currentCount(CHARGE_TEST_CURRENT_COUNT)
				.build();

		Charge charge1 = Charge.builder()
				.chargePlaceId(CHARGE_TEST_ID1)
				.name(CHARGE_TEST_NAME1)
				.totalCount(CHARGE_TEST_TOTAL_COUNT)
				.currentCount(CHARGE_TEST_CURRENT_COUNT)
				.build();


		chargeRepository.save(charge);
		chargeRepository.save(charge1);

		System.out.println("Test charge place id = " + CHARGE_TEST_ID);
		System.out.println("Test charge1 place id = " + CHARGE_TEST_ID1);

		Reservation reservation = Reservation.builder()
				.time(LocalDateTime.now())
				.charge(charge)
				.user(user)
				.reservationValidationId(RESERVATION_TEST_UUID)
				.build();

		Reservation reservation1 = Reservation.builder()
				.time(LocalDateTime.now())
				.charge(charge1)
				.user(user)
				.reservationValidationId(RESERVATION_TEST_UUID1)
				.build();

		Reservation reservation2 = Reservation.builder()
				.time(LocalDateTime.now())
				.charge(charge)
				.user(admin)
				.reservationValidationId(RESERVATION_TEST_ADMIN_UUID)
				.build();

		Reservation save = reservationRepository.save(reservation);
		Reservation save1 = reservationRepository.save(reservation1);
		Reservation save2 = reservationRepository.save(reservation2);

		String reservationValidationId = save.getReservationValidationId();
		String reservationValidationId1 = save1.getReservationValidationId();

		System.out.println("Test reservation user email = " + user.getEmail());
		System.out.println("Test reservationValidationId = " + reservationValidationId);
		System.out.println("Test reservationValidationId = " + reservationValidationId1);

		String reservationValidationId2 = save2.getReservationValidationId();
		System.out.println("Test reservation admin email = " + admin.getEmail());
		System.out.println("Test reservationValidationId = " + reservationValidationId2);
	}

}
