package com.portfolio.gascharge.init;

import com.portfolio.gascharge.config.properties.AppProperties;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.token.Token;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.oauth.token.TokenProvider;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.repository.reservation.ReservationRepository;
import com.portfolio.gascharge.repository.token.TokenRepository;
import com.portfolio.gascharge.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.portfolio.gascharge.domain.charge.ChargeTestData.*;
import static com.portfolio.gascharge.domain.reservation.ReservationTestData.*;
import static com.portfolio.gascharge.domain.user.UserTestData.ADMIN_TEST_EMAIL;
import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_EMAIL;

@Component
@Transactional
@Profile({"default", "real"})
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
    private TokenRepository tokenRepository;
    @Autowired
    private AppProperties appProperties;
    @EventListener(ApplicationReadyEvent.class)
    public void initTestValue() {
        Optional<User> byEmail = userRepository.findByEmail(USER_TEST_EMAIL);

        User user = null;

        if (byEmail.isEmpty()) {
            user = UserTestData.getCloneUser();
            userRepository.save(user);
        } else {
            user = byEmail.get();
        }

        Optional<User> byEmail1 = userRepository.findByEmail(ADMIN_TEST_EMAIL);

        User admin = null;

        if (byEmail1.isEmpty()) {
            admin = UserTestData.getCloneAdmin();
            userRepository.save(admin);
        } else {
            admin = byEmail1.get();
        }

        String token = tokenProvider.jwtTokenBuild(user.getId().toString(), tokenProvider.getLongExpiryDay(), appProperties.getAuth().getTokenSecret());
        String tokenAdmin = tokenProvider.jwtTokenBuild(admin.getId().toString(), tokenProvider.getLongExpiryDay(), appProperties.getAuth().getTokenSecret());

        System.out.println("Test id token = Bearer " + token);
        System.out.println("Admin test id token = Bearer " + tokenAdmin);

        Optional<Token> byName = tokenRepository.findByName("유저");

        if (byName.isEmpty()) {
            Token userToken = new Token();
            userToken.setName("유저");
            userToken.setJwtToken("Bearer " + token);
            tokenRepository.save(userToken);
        } else {
            byName.get().setJwtToken("Bearer " + token);
        }

        Optional<Token> byName1 = tokenRepository.findByName("어드민");

        if (byName1.isEmpty()) {
            Token adminToken = new Token();
            adminToken.setName("어드민");
            adminToken.setJwtToken("Bearer " + tokenAdmin);
            tokenRepository.save(adminToken);
        } else {
            byName1.get().setJwtToken("Bearer " + tokenAdmin);
        }

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
