package com.portfolio.gascharge.controller.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.gascharge.config.security.SecurityConfig;
import com.portfolio.gascharge.controller.reservation.dto.ForceUpdateReservationRequestDto;
import com.portfolio.gascharge.controller.reservation.dto.PostReserveRequestDto;
import com.portfolio.gascharge.controller.reservation.dto.UpdateReservationRequestDto;
import com.portfolio.gascharge.controller.user.PrincipalDetailsArgumentResolver;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.ChargeTestData;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.enums.reservation.ReservationStatus;
import com.portfolio.gascharge.error.exception.GlobalExceptionHandler;
import com.portfolio.gascharge.error.exception.HandleException;
import com.portfolio.gascharge.oauth.filter.TokenAuthenticationFilter;
import com.portfolio.gascharge.service.reservation.ReservationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.portfolio.gascharge.utils.DateTimeFormat.formatter;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private Reservation reservationTestAdmin;
    private Reservation reservationTestUser;

    @BeforeEach
    void setMockMvc() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(reservationController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver(UserTestData.getCloneAdmin()), new PageableHandlerMethodArgumentResolver())
                .build();

        Charge charge = ChargeTestData.getTestCharge();
        LocalDateTime now = LocalDateTime.now().plusDays(1);

        this.reservationTestAdmin = Reservation.builder()
                .reservationValidationId(UUID.randomUUID().toString())
                .user(UserTestData.getCloneAdmin())
                .charge(charge)
                .time(now)
                .build();

        this.reservationTestUser = Reservation.builder()
                .reservationValidationId(UUID.randomUUID().toString())
                .user(UserTestData.getCloneUser())
                .charge(charge)
                .time(now)
                .build();
    }

    @Test
    void reserve() throws Exception {
        // given



        PostReserveRequestDto postReserveRequestDto = PostReserveRequestDto.builder()
                .chargePlaceId(this.reservationTestAdmin.getCharge().getChargePlaceId())
                .time(this.reservationTestAdmin.getTime())
                .build();

        // when
        when(reservationService.reserve(any(), any(), any())).thenReturn(this.reservationTestAdmin);

        // then
        this.mvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReserveRequestDto)))
                .andExpect(jsonPath("$.reservationId").value(this.reservationTestAdmin.getReservationValidationId()))
                .andExpect(jsonPath("$.userEmail").value(this.reservationTestAdmin.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(this.reservationTestAdmin.getCharge().getChargePlaceId()))
                .andExpect(jsonPath("$.reserveTime").value(this.reservationTestAdmin.getTime().format(formatter())))
                .andExpect(jsonPath("$.status").value(this.reservationTestAdmin.getStatus().name()))

                .andExpect(status().isCreated());
    }

    @Test
    void updateSelfReservationTime() throws Exception {
        // given
        LocalDateTime updateTime = this.reservationTestAdmin.getTime().plusHours(1);
        UpdateReservationRequestDto updateReservationRequestDto = new UpdateReservationRequestDto();
        updateReservationRequestDto.setReservationValidationId(this.reservationTestAdmin.getReservationValidationId());
        updateReservationRequestDto.setEmail(this.reservationTestAdmin.getUser().getEmail());
        updateReservationRequestDto.setUpdateTime(updateTime);

        String format = updateTime.format(formatter());

        Reservation reservationTest1 = this.reservationTestAdmin;
        reservationTest1.updateTime(updateTime);

        // when
        when(reservationService.updateTime(any(), any())).thenReturn(reservationTest1);

        // then
        mvc.perform(patch("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReservationRequestDto)))
                .andExpect(jsonPath("$.reservationId").value(this.reservationTestAdmin.getReservationValidationId()))
                .andExpect(jsonPath("$.userEmail").value(this.reservationTestAdmin.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(this.reservationTestAdmin.getCharge().getChargePlaceId()))
                .andExpect(jsonPath("$.reserveTime").value(format))
                .andExpect(jsonPath("$.status").value(this.reservationTestAdmin.getStatus().name()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void forceUpdate() throws Exception {
        // given
        ReservationStatus cancel = ReservationStatus.CANCEL;
        LocalDateTime time = LocalDateTime.now().plusHours(1);
        String format = time.format(formatter());

        Reservation reservationTest1 = this.reservationTestAdmin;
        reservationTest1.updateStatus(cancel);
        reservationTest1.updateTime(time);

        ForceUpdateReservationRequestDto forceUpdateReservationRequestDto = new ForceUpdateReservationRequestDto();
        forceUpdateReservationRequestDto.setStatus(cancel);
        forceUpdateReservationRequestDto.setTime(time);

        // when
        when(reservationService.updateDynamicField(any(), any())).thenReturn(reservationTest1);

        // then
        mvc.perform(patch("/reservation/" + reservationTest1.getReservationValidationId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forceUpdateReservationRequestDto)))
                .andExpect(jsonPath("$.reservationId").value(reservationTest1.getReservationValidationId()))
                .andExpect(jsonPath("$.userEmail").value(reservationTest1.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(reservationTest1.getCharge().getChargePlaceId()))
                .andExpect(jsonPath("$.reserveTime").value(format))
                .andExpect(jsonPath("$.status").value(reservationTest1.getStatus().name()))

                .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        // given

        // when
        when(reservationService.findByReservationValidationId(this.reservationTestAdmin.getReservationValidationId())).thenReturn(this.reservationTestAdmin);

        // then
        mvc.perform(get("/reservation/" + this.reservationTestAdmin.getReservationValidationId()))
                .andExpect(jsonPath("$.reservationId").value(this.reservationTestAdmin.getReservationValidationId()))
                .andExpect(jsonPath("$.userEmail").value(this.reservationTestAdmin.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(this.reservationTestAdmin.getCharge().getChargePlaceId()))
                .andExpect(jsonPath("$.reserveTime").value(this.reservationTestAdmin.getTime().format(formatter())))
                .andExpect(jsonPath("$.status").value(this.reservationTestAdmin.getStatus().name()))

                .andExpect(status().isOk());
    }

    @Test
    void getList() throws Exception {
        // given email 파라미터
        String email = this.reservationTestAdmin.getUser().getEmail();
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(this.reservationTestAdmin);

        ResultMatcher[] matchers = new ResultMatcher[] {
                jsonPath("$.content[0].reservationId").value(this.reservationTestAdmin.getReservationValidationId()),
                jsonPath("$.content[0].userEmail").value(this.reservationTestAdmin.getUser().getEmail()),
                jsonPath("$.content[0].chargePlaceId").value(this.reservationTestAdmin.getCharge().getChargePlaceId()),
                jsonPath("$.content[0].reserveTime").value(this.reservationTestAdmin.getTime().format(formatter())),
                jsonPath("$.content[0].status").value(this.reservationTestAdmin.getStatus().name())
        };

        ResultMatcher[] matchers1 = new ResultMatcher[] {
                jsonPath("$.content[1].reservationId").value(this.reservationTestUser.getReservationValidationId()),
                jsonPath("$.content[1].userEmail").value(this.reservationTestUser.getUser().getEmail()),
                jsonPath("$.content[1].chargePlaceId").value(this.reservationTestUser.getCharge().getChargePlaceId()),
                jsonPath("$.content[1].reserveTime").value(this.reservationTestUser.getTime().format(formatter())),
                jsonPath("$.content[1].status").value(this.reservationTestUser.getStatus().name())
        };

        Page<Reservation> pageImpl = new PageImpl<>(reservations);

        // when
        when(reservationService.findAll(anyString(), isNull(), any(Pageable.class))).thenReturn(pageImpl);

        // then
        String urlTemplate = "/reservation";

        mvc.perform(get(urlTemplate)
                        .param("email", email))
                .andExpect(jsonPath("$.content.size()").value(1))

                .andExpectAll(matchers)

                .andExpect(status().isOk());

        // given chargePlaceId 파라미터
        String chargePlaceId = this.reservationTestUser.getCharge().getChargePlaceId();
        reservations.add(this.reservationTestUser);

        pageImpl = new PageImpl<>(reservations);

        // when
        when(reservationService.findAll(isNull(), anyString(), any(Pageable.class))).thenReturn(pageImpl);

        // then
        mvc.perform(get(urlTemplate)
                        .param("chargePlaceId", chargePlaceId))
                .andExpect(jsonPath("$.content.size()").value(2))

                .andExpectAll(matchers)

                .andExpectAll(matchers1)

                .andExpect(status().isOk());

        // given page size sort 파라미터
        String page = "0";
        String size = "10";
        String sort = "email,DESC";

        // when
        when(reservationService.findAll(isNull(), isNull(), any(Pageable.class))).thenReturn(pageImpl);

        // then
        mvc.perform(get(urlTemplate)
                        .param("page", page)
                        .param("size", size)
                        .param("sort", sort))
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.pageable.pageSize").value(size))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(true))

                .andExpectAll(matchers)

                .andExpectAll(matchers1)

                .andDo(print())
                .andExpect(status().isOk());

        // given not matched chargePlaceId
        chargePlaceId = "dump";
        pageImpl = new PageImpl<>(new ArrayList<>());

        // when
        when(reservationService.findAll(isNull(), anyString(), any(Pageable.class))).thenReturn(pageImpl);

        // then
        mvc.perform(get(urlTemplate)
                        .param("chargePlaceId", chargePlaceId))
                .andExpect(jsonPath("$.content").isEmpty())

                .andExpect(status().isOk());
    }

    @Test
    void cancelWithAdmin() throws Exception {
        // given 어드민
        String reservationValidationId = this.reservationTestAdmin.getReservationValidationId();
        ReservationStatus status = ReservationStatus.CANCEL;

        this.reservationTestAdmin.updateStatus(status);

        // when
        when(reservationService.updateStatus(anyString(), any(ReservationStatus.class))).thenReturn(this.reservationTestAdmin);

        // then
        mvc.perform(post("/reservation/cancel")
                        .param("reservationValidationId", reservationValidationId))
                .andExpect(jsonPath("$.reservationValidationId").value(reservationValidationId))
                .andExpect(jsonPath("$.userEmail").value(this.reservationTestAdmin.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(this.reservationTestAdmin.getCharge().getChargePlaceId()))
                .andExpect(jsonPath("$.reserveTime").value(this.reservationTestAdmin.getTime().format(formatter())))
                .andExpect(jsonPath("$.status").value(status.name()))

                .andExpect(status().isOk());
    }

    @Test
    void cancel() throws Exception {
        // given mockmvc 유저
        mvc = MockMvcBuilders
                .standaloneSetup(reservationController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver(UserTestData.getCloneUser()), new PageableHandlerMethodArgumentResolver())
                .build();

        Reservation reservation = this.reservationTestUser;

        String email = reservation.getUser().getEmail();
        String reservationValidationId = this.reservationTestAdmin.getReservationValidationId();

        // when 이메일 동일 여부 체크 통과 안됨
        when(reservationService.checkSameEmail(anyString(), anyString())).thenReturn(false);

        // then
        String urlTemplate = "/reservation/cancel";
        Assertions.assertThatThrownBy(() ->
                        mvc.perform(post(urlTemplate)
                                        .param("email", email)
                                        .param("reservationValidationId", reservationValidationId))
                                .andExpect(status().isOk()))
                .hasCauseInstanceOf(AccessDeniedException.class);

        // given mvc 유저
        ReservationStatus status = ReservationStatus.CANCEL;

        reservation.updateStatus(status);

        // when 이메일 동일 여부 체크 통과, 업데이트 로직 실행
        when(reservationService.checkSameEmail(anyString(), anyString())).thenReturn(true);
        when(reservationService.updateStatus(anyString(), any(ReservationStatus.class))).thenReturn(reservation);

        // then
        mvc.perform(post(urlTemplate)
                        .param("email", email)
                        .param("reservationValidationId", reservationValidationId))
                .andExpect(jsonPath("$.reservationValidationId").value(reservation.getReservationValidationId()))
                .andExpect(jsonPath("$.userEmail").value(reservation.getUser().getEmail()))
                .andExpect(jsonPath("$.chargePlaceId").value(reservation.getCharge().getChargePlaceId()))
                .andExpect(jsonPath("$.reserveTime").value(reservation.getTime().format(formatter())))
                .andExpect(jsonPath("$.status").value(status.name()))

                .andExpect(status().isOk());
    }
}
