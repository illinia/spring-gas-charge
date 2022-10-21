package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeSearchStatus;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.service.charge.ChargeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.portfolio.gascharge.domain.user.UserTestData.USER_TEST_EMAIL;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChargeControllerTest {

    @Autowired
    ChargeRepository chargeRepository;

    @BeforeEach
    void set() {
        for (int i = 0; i < 10; i++) {
            Charge charge = Charge.builder()
                    .chargePlaceId("test" + String.valueOf(i))
                    .name("name" + String.valueOf(i))
                    .totalCount(Long.valueOf(i))
                    .currentCount(Long.valueOf(i))
                    .build();
            chargeRepository.save(charge);
        }

    }

//    @MockBean
//    ChargeService chargeService;

    @Autowired
    MockMvc mvc;

    @Test
    @WithUserDetails(USER_TEST_EMAIL)
    void getChargeList() throws Exception {
        // 확인해야할 정보 String name, ChargePlaceMembership isMembership
        // Pageable page, size, sort
        mvc.perform(get("/charge")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
                        )
                .andExpect(status().isOk());

//        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
//        ArgumentCaptor<ChargeSearchStatus> chargeSearchCaptor = ArgumentCaptor.forClass(ChargeSearchStatus.class);
//        verify(chargeService).findAll(chargeSearchCaptor.capture(), pageableCaptor.capture());
//
//        Pageable pageableCaptorValue = pageableCaptor.getValue();
//        ChargeSearchStatus chargeSearchCaptorValue = chargeSearchCaptor.getValue();
//
//        System.out.println(pageableCaptorValue);
//        System.out.println(chargeSearchCaptorValue);
    }
}
