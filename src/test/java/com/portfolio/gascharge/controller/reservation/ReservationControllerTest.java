package com.portfolio.gascharge.controller.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.gascharge.config.security.SecurityConfig;
import com.portfolio.gascharge.controller.reservation.dto.PostReserveRequestDto;
import com.portfolio.gascharge.controller.user.PrincipalDetailsArgumentResolver;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.ChargeTestData;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.error.exception.GlobalExceptionHandler;
import com.portfolio.gascharge.error.exception.HandleException;
import com.portfolio.gascharge.oauth.filter.TokenAuthenticationFilter;
import com.portfolio.gascharge.service.reservation.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ReservationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, TokenAuthenticationFilter.class, GlobalExceptionHandler.class, HandleException.class})
        }
)
public class ReservationControllerTest {

    @MockBean
    ReservationService reservationService;
    @Autowired
    ReservationController reservationController;
    @Autowired
    ObjectMapper objectMapper;
    private MockMvc mvc;

    @BeforeEach
    void setMockMvc() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(reservationController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver(UserTestData.getCloneAdmin()), new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void reserve() throws Exception {
        // given
        Charge charge = ChargeTestData.getTestCharge1();
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        Reservation reservation = Reservation.builder()
                .reservationValidationId(UUID.randomUUID().toString())
                .user(UserTestData.getCloneAdmin())
                .charge(charge)
                .time(now)
                .build();

        PostReserveRequestDto postReserveRequestDto = PostReserveRequestDto.builder()
                .chargePlaceId(charge.getChargePlaceId())
                .time(now)
                .build();

        // when
        when(reservationService.reserve(any(), any(), any())).thenReturn(reservation);

        // then
        this.mvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReserveRequestDto)))
                .andExpect(jsonPath("$.reservationId").value(reservation.getReservationValidationId()))
                .andExpect(jsonPath("$.userEmail").value(reservation.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(reservation.getCharge().getChargePlaceId()))
//                .andExpect(jsonPath(LocalDateTime.form).value(reservation.getTime()))
                .andExpect(jsonPath("$.status").value(reservation.getStatus().name()))

                .andExpect(status().isCreated());
    }
}
