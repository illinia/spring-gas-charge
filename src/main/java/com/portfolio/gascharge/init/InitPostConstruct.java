package com.portfolio.gascharge.init;

import com.portfolio.gascharge.config.properties.AppProperties;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.portfolio.gascharge.domain.charge.ChargeTestData.*;
import static com.portfolio.gascharge.domain.reservation.ReservationTestData.*;
import static com.portfolio.gascharge.domain.user.UserTestData.*;

@Component
public class InitPostConstruct {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ChargeRepository chargeRepository;
    @Autowired
    private AppProperties appProperties;


    @PostConstruct
    @Transactional
    public void initTestValue() {
        Optional<User> byEmail = userRepository.findByEmail(USER_TEST_EMAIL1);

        User user = null;

        if (byEmail.isEmpty()) {
            user = User.builder()
                    .name(USER_TEST_NAME1)
                    .email(USER_TEST_EMAIL1)
                    .provider(AuthProvider.google)
                    .build();
            userRepository.save(user);
        } else {
            user = byEmail.get();
        }

        Optional<User> byEmail1 = userRepository.findByEmail(USER_TEST_EMAIL2);

        User admin = null;

        if (byEmail1.isEmpty()) {
            admin = User.builder()
                    .name(USER_TEST_NAME2)
                    .email(USER_TEST_EMAIL2)
                    .emailVerified(UserEmailVerified.VERIFIED)
                    .provider(AuthProvider.google)
                    .userAuthority(UserAuthority.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        } else {
            admin = byEmail1.get();
        }

        String token = tokenProvider.jwtTokenBuild(user.getId().toString(), tokenProvider.getLongExpiryDay(), appProperties.getAuth().getTokenSecret());
        String tokenAdmin = tokenProvider.jwtTokenBuild(admin.getId().toString(), tokenProvider.getLongExpiryDay(), appProperties.getAuth().getTokenSecret());

        System.out.println("Test id token = Bearer " + token);
        System.out.println("Admin test id token = Bearer " + tokenAdmin);

        Optional<Charge> byChargePlaceId = chargeRepository.findByChargePlaceId(CHARGE_TEST_ID);

        Charge charge = null;

        if (byChargePlaceId.isEmpty()) {
            charge = Charge.builder()
                    .chargePlaceId(CHARGE_TEST_ID)
                    .name(CHARGE_TEST_NAME)
                    .totalCount(CHARGE_TEST_TOTAL_COUNT)
                    .currentCount(CHARGE_TEST_CURRENT_COUNT)
                    .build();
            chargeRepository.save(charge);
        } else {
            charge = byChargePlaceId.get();
        }

        Optional<Charge> byChargePlaceId1 = chargeRepository.findByChargePlaceId(CHARGE_TEST_ID1);

        Charge charge1 = null;

        if (byChargePlaceId1.isEmpty()) {
            charge1 = Charge.builder()
                    .chargePlaceId(CHARGE_TEST_ID1)
                    .name(CHARGE_TEST_NAME1)
                    .totalCount(CHARGE_TEST_TOTAL_COUNT)
                    .currentCount(CHARGE_TEST_CURRENT_COUNT)
                    .build();
            chargeRepository.save(charge1);
        } else {
            charge1 = byChargePlaceId1.get();
        }

        System.out.println("Test charge place id = " + CHARGE_TEST_ID);
        System.out.println("Test charge1 place id = " + CHARGE_TEST_ID1);

        Optional<Reservation> byReservationValidationId = reservationRepository.findByReservationValidationId(RESERVATION_TEST_UUID);

        Reservation reservation = null;

        if (byReservationValidationId.isEmpty()) {
            reservation = Reservation.builder()
                    .time(LocalDateTime.now())
                    .charge(charge)
                    .user(user)
                    .reservationValidationId(RESERVATION_TEST_UUID)
                    .build();
            reservationRepository.save(reservation);
        } else {
            reservation = byReservationValidationId.get();
        }

        Optional<Reservation> byReservationValidationId1 = reservationRepository.findByReservationValidationId(RESERVATION_TEST_UUID1);

        Reservation reservation1 = null;

        if (byReservationValidationId1.isEmpty()) {
            reservation1 = Reservation.builder()
                    .time(LocalDateTime.now())
                    .charge(charge1)
                    .user(user)
                    .reservationValidationId(RESERVATION_TEST_UUID1)
                    .build();
            reservationRepository.save(reservation1);
        } else {
            reservation1 = byReservationValidationId1.get();
        }

        Optional<Reservation> byReservationValidationId2 = reservationRepository.findByReservationValidationId(RESERVATION_TEST_ADMIN_UUID);

        Reservation reservation2 = null;

        if (byReservationValidationId2.isEmpty()) {
            reservation2 = Reservation.builder()
                    .time(LocalDateTime.now())
                    .charge(charge)
                    .user(admin)
                    .reservationValidationId(RESERVATION_TEST_ADMIN_UUID)
                    .build();
            reservationRepository.save(reservation2);
        }

        System.out.println("Test reservation user email = " + user.getEmail());
        System.out.println("Test RESERVATION_TEST_UUID = " + RESERVATION_TEST_UUID);
        System.out.println("Test RESERVATION_TEST_UUID1 = " + RESERVATION_TEST_UUID1);

        System.out.println("Test reservation admin email = " + admin.getEmail());
        System.out.println("Test RESERVATION_TEST_ADMIN_UUID = " + RESERVATION_TEST_ADMIN_UUID);
    }
}
