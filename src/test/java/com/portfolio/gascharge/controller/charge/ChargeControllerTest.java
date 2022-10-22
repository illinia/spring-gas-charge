package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.config.security.SecurityConfig;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeSearchStatus;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.error.exception.GlobalExceptionHandler;
import com.portfolio.gascharge.error.exception.HandleException;
import com.portfolio.gascharge.oauth.filter.TokenAuthenticationFilter;
import com.portfolio.gascharge.service.charge.ChargeService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ChargeController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, TokenAuthenticationFilter.class})
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GlobalExceptionHandler.class, HandleException.class})
        }
)
public class ChargeControllerTest {

    @MockBean
    ChargeService chargeService;
    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
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
        mvc.perform(get("/charge")
                        .param("name", name))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].name").value(name))

                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
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
        mvc.perform(get("/charge")
                        .param("is-membership", ChargePlaceMembership.MEMBERSHIP.toString()))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].membership").value(ChargePlaceMembership.MEMBERSHIP.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getChargeListWithNotMembership() throws Exception {
        // given not-membership converter 없이

        // when
        Page<Charge> charges = new PageImpl<>(new ArrayList<>());
        when(chargeService.findAll(any(ChargeSearchStatus.class), any(Pageable.class))).thenReturn(charges);

        // then
        mvc.perform(get("/charge")
                        .param("is-membership", ChargePlaceMembership.NOT_MEMBERSHIP.toString()))
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
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
        mvc.perform(get("/charge")
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

//    @Test
//    @WithMockUser
//    void getChargeByChargePlaceId() {
//        // given
//        List<Charge> chargeList = new ArrayList<>();
//
//        Charge.builder()
//                .
//
//    }
}
