package com.portfolio.gascharge.repository.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeSearchStatus;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChargeRepositoryTest {

    @Autowired
    ChargeRepository chargeRepository;

    @Test
    void findChargeWithSearchStatus() {
        Charge charge = Charge.builder()
                .chargePlaceId("testChargePlaceId")
                .name("testChargePlaceName")
                .totalCount(10L)
                .currentCount(5L)
                .membership(ChargePlaceMembership.MEMBERSHIP)
                .build();

        Charge charge1 = Charge.builder()
                .chargePlaceId("testChargePlaceId1")
                .name("testChargePlaceName1")
                .totalCount(4L)
                .currentCount(2L)
                .membership(ChargePlaceMembership.NOT_MEMBERSHIP)
                .build();

        Charge savedCharge = chargeRepository.save(charge);
        Charge savedCharge1 = chargeRepository.save(charge1);

        // given default
        ChargeSearchStatus chargeSearchStatus = new ChargeSearchStatus();

        int page = 0;
        int size = 10;
        Sort sort = Sort.by("name").ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // when
        Page<Charge> result = chargeRepository.findChargeWithSearchStatus(chargeSearchStatus, pageRequest);

        // then
        assertThat(result.getPageable().getPageNumber()).isEqualTo(page);
        assertThat(result.getPageable().getPageSize()).isEqualTo(size);
        assertThat(result.getPageable().getSort()).isEqualTo(sort);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).toString()).isEqualTo(savedCharge.toString());
        assertThat(result.getContent().get(1).toString()).isEqualTo(savedCharge1.toString());

        // given name 1
        chargeSearchStatus.setName("1");

        // when
        Page<Charge> result1 = chargeRepository.findChargeWithSearchStatus(chargeSearchStatus, pageRequest);

        // then
        assertThat(result1.getContent()).hasSize(1);
        assertThat(result1.getContent().get(0).toString()).isEqualTo(charge1.toString());

        // given name null, membership
        chargeSearchStatus.setChargePlaceMembership(ChargePlaceMembership.MEMBERSHIP);
        chargeSearchStatus.setName(null);

        // when
        Page<Charge> result2 = chargeRepository.findChargeWithSearchStatus(chargeSearchStatus, pageRequest);

        // then
        assertThat(result2.getContent()).hasSize(1);
        assertThat(result2.getContent().get(0).toString()).isEqualTo(charge.toString());

        // given
        chargeSearchStatus.setChargePlaceMembership(null);
        chargeSearchStatus.setName(null);

        int page1 = 1;
        int size1 = 20;
        Sort sort1 = Sort.by("totalCount").descending();
        PageRequest pageRequest1 = PageRequest.of(page1, size1, sort1);

        // when
        Page<Charge> result3 = chargeRepository.findChargeWithSearchStatus(chargeSearchStatus, pageRequest1);

        // then
        assertThat(result3.getPageable().getPageNumber()).isEqualTo(page1);
        assertThat(result3.getPageable().getPageSize()).isEqualTo(size1);
        assertThat(result3.getPageable().getSort()).isEqualTo(sort1);
        assertThat(result3.getContent()).hasSize(0);
    }
}