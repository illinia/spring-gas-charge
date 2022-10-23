package com.portfolio.gascharge.controller.charge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.gascharge.config.security.SecurityConfig;
import com.portfolio.gascharge.controller.charge.dto.AddChargeRequestDto;
import com.portfolio.gascharge.controller.charge.dto.UpdateChargeRequestDto;
import com.portfolio.gascharge.controller.user.PrincipalDetailsArgumentResolver;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.ChargeTestData;
import com.portfolio.gascharge.domain.charge.search.ChargeSearchStatus;
import com.portfolio.gascharge.domain.user.UserTestData;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.error.exception.GlobalExceptionHandler;
import com.portfolio.gascharge.error.exception.HandleException;
import com.portfolio.gascharge.oauth.filter.TokenAuthenticationFilter;
import com.portfolio.gascharge.service.charge.ChargeService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ChargeController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, TokenAuthenticationFilter.class, GlobalExceptionHandler.class, HandleException.class})
        }
)
public class ChargeControllerTest {

    @MockBean
    ChargeService chargeService;
    @Autowired
    ChargeController chargeController;
    @Autowired
    ObjectMapper objectMapper;
    private MockMvc mvc;

    @BeforeEach
    void setMockMvc() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(chargeController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver(UserTestData.getCloneAdmin()), new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getChargeListWithName() throws Exception {
        // given name
        List<Charge> chargeList = new ArrayList<>();

        String name = "membershipCharge";
        chargeList.add(Charge.builder()
                .chargePlaceId("membershipTest")
                .name(name)
                .totalCount(5L)
                .currentCount(5L)
                .membership(ChargePlaceMembership.MEMBERSHIP)
                .build()
        );

        Page<Charge> charges = new PageImpl<>(chargeList);

        // when
        when(chargeService.findAll(any(ChargeSearchStatus.class), any(Pageable.class))).thenReturn(charges);

        // then
        this.mvc.perform(get("/charge")
                        .param("name", name))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].name").value(name))

                .andExpect(status().isOk());
    }

    @Test
    void getChargeListWithMembership() throws Exception {
        // given membership
        List<Charge> chargeList = new ArrayList<>();

        String name = "membershipCharge";
        chargeList.add(Charge.builder()
                .chargePlaceId("membershipTest")
                .name(name)
                .totalCount(5L)
                .currentCount(5L)
                .membership(ChargePlaceMembership.MEMBERSHIP)
                .build()
        );

        Page<Charge> charges = new PageImpl<>(chargeList);

        // when
        when(chargeService.findAll(any(ChargeSearchStatus.class), any(Pageable.class))).thenReturn(charges);

        // then converter 없이
        this.mvc.perform(get("/charge")
                        .param("is-membership", ChargePlaceMembership.MEMBERSHIP.toString()))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].membership").value(ChargePlaceMembership.MEMBERSHIP.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void getChargeListWithNotMembership() throws Exception {
        // given not-membership converter 없이

        // when
        Page<Charge> charges = new PageImpl<>(new ArrayList<>());
        when(chargeService.findAll(any(ChargeSearchStatus.class), any(Pageable.class))).thenReturn(charges);

        // then
        this.mvc.perform(get("/charge")
                        .param("is-membership", ChargePlaceMembership.NOT_MEMBERSHIP.toString()))
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void getChargeListWithPageable() throws Exception {
        // given Pageable page, size, sort
        List<Charge> chargeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Charge charge = Charge.builder()
                    .chargePlaceId("test" + i)
                    .name("name" + i)
                    .totalCount(Long.valueOf(i))
                    .currentCount(Long.valueOf(i))
                    .build();
            chargeList.add(charge);
        }

        int page = 0;
        int size = 10;
        Sort sort = Sort.by("name").descending();

        Page<Charge> charges = new PageImpl<>(chargeList);

        // when
        when(chargeService.findAll(any(ChargeSearchStatus.class), any(Pageable.class))).thenReturn(charges);

        // then
        this.mvc.perform(get("/charge")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort.toString())
                        )
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.pageable.pageSize").value(size))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(jsonPath("$.content.size()").value(Integer.valueOf(size)))

                .andExpect(status().isOk());
    }

    @Test
    void getChargeByChargePlaceId() throws Exception {
        // given
        Charge testCharge1 = ChargeTestData.getTestCharge();

        // when
        when(chargeService.findByChargePlaceId(anyString())).thenReturn(testCharge1);

        // then
        this.mvc.perform(get("/charge/" + testCharge1.getChargePlaceId()))
                .andExpect(jsonPath("$.chargePlaceId").value(testCharge1.getChargePlaceId()))
                .andExpect(jsonPath("$.name").value(testCharge1.getName()))
                .andExpect(jsonPath("$.totalCount").value(testCharge1.getTotalCount()))
                .andExpect(jsonPath("$.currentCount").value(testCharge1.getCurrentCount()))

                .andExpect(status().isOk());
    }

    @Test
    void addCharge() throws Exception {
        // given
        Charge testCharge1 = ChargeTestData.getTestCharge();
        AddChargeRequestDto addChargeRequestDto = new AddChargeRequestDto();
        addChargeRequestDto.setChargePlaceId(testCharge1.getChargePlaceId());
        addChargeRequestDto.setName(testCharge1.getName());
        addChargeRequestDto.setTotalCount(testCharge1.getTotalCount());
        addChargeRequestDto.setCurrentCount(testCharge1.getCurrentCount());

        // when
        when(chargeService.saveCharge(any(Charge.class))).thenReturn(testCharge1);

        // then
        this.mvc.perform(post("/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addChargeRequestDto)))
                .andExpect(jsonPath("$.chargePlaceId").value(testCharge1.getChargePlaceId()))
                .andExpect(jsonPath("$.name").value(testCharge1.getName()))
                .andExpect(jsonPath("$.totalCount").value(testCharge1.getTotalCount()))
                .andExpect(jsonPath("$.currentCount").value(testCharge1.getCurrentCount()))
                .andExpect(jsonPath("$.membership").value(testCharge1.getMembership().toString()))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCharge() throws Exception {
        // given
        Charge testCharge1 = ChargeTestData.getTestCharge();
        String chargePlaceId = testCharge1.getChargePlaceId();

        UpdateChargeRequestDto updateChargeRequestDto = new UpdateChargeRequestDto();
        String newTestName = "newTestName";
        long totalCount = 10L;
        long currentCount = 1L;
        updateChargeRequestDto.setName(newTestName);
        updateChargeRequestDto.setTotalCount(totalCount);
        updateChargeRequestDto.setCurrentCount(currentCount);

        Charge newCharge = Charge.builder()
                .chargePlaceId(chargePlaceId)
                .name(newTestName)
                .totalCount(totalCount)
                .currentCount(currentCount)
                .build();

        // when
        when(chargeService.updateDynamicField(anyString(), any())).thenReturn(newCharge);

        // then
        this.mvc.perform(patch("/charge/" + chargePlaceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateChargeRequestDto)))
                .andExpect(jsonPath("$.chargePlaceId").value(chargePlaceId))
                .andExpect(jsonPath("$.name").value(newTestName))
                .andExpect(jsonPath("$.totalCount").value(totalCount))
                .andExpect(jsonPath("$.currentCount").value(currentCount))

                .andExpect(status().isOk());
    }

    @Test
    void deleteCharge() throws Exception {
        // given
        String chargePlaceId = "testId";
        String result = "Delete " + chargePlaceId + " Success";

        // when
        when(chargeService.deleteCharge(anyString())).thenReturn(result);

        // then
        this.mvc.perform(delete("/charge/" + chargePlaceId))
                .andExpect(content().string(result))
                .andExpect(status().isNoContent());
    }
}
