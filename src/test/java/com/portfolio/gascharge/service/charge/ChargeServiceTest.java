package com.portfolio.gascharge.service.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.ChargeTestData;
import com.portfolio.gascharge.domain.charge.search.ChargeSearchStatus;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChargeServiceTest {

    @InjectMocks
    ChargeService chargeService;
    @Mock
    ChargeRepository chargeRepository;

    private Charge charge;
    private Charge charge1;

    @BeforeEach
    void setCharge() {
        this.charge = ChargeTestData.getTestCharge();
        this.charge1 = ChargeTestData.getTestCharge1();
    }

    @Test
    void findByChargePlaceId() {
        // given charge
        String chargePlaceId = this.charge.getChargePlaceId();

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.of(this.charge));
        Charge byId = chargeService.findByChargePlaceId(chargePlaceId);

        // then
        assertThat(byId).isEqualTo(this.charge);

        // given charge1
        String chargePlaceId1 = this.charge1.getChargePlaceId();

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.of(this.charge1));
        Charge byId1 = chargeService.findByChargePlaceId(chargePlaceId1);

        // then
        assertThat(byId1).isEqualTo(this.charge1);

        // given wrong id
        String wrongId = "dump";

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> chargeService.findByChargePlaceId(wrongId))
                .isInstanceOf(NoEntityFoundException.class);
    }

    @Test
    void saveCharge() {
        // given charge
        Charge saveCharge = this.charge;

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.empty());
        when(chargeRepository.save(any(Charge.class))).thenReturn(saveCharge);
        Charge result = chargeService.saveCharge(saveCharge);

        // then
        assertThat(result).isEqualTo(saveCharge);

        // given duplicated charge

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.of(saveCharge));

        // then
        assertThatThrownBy(() -> chargeService.saveCharge(saveCharge))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void findAll() {
        // given
        ChargeSearchStatus chargeSearchStatus = new ChargeSearchStatus();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by("name").ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<Charge> charges = new ArrayList<>();
        charges.add(this.charge);
        charges.add(this.charge1);
        Page<Charge> pageImpl = new PageImpl<>(charges, pageRequest, charges.size());

        // when
        when(chargeRepository.findChargeWithSearchStatus(any(ChargeSearchStatus.class), any(Pageable.class))).thenReturn(pageImpl);
        Page<Charge> result = chargeService.findAll(chargeSearchStatus, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(page);
        assertThat(result.getPageable().getPageSize()).isEqualTo(size);
        assertThat(result.getPageable().getSort()).isEqualTo(sort);
    }

    @Test
    void updateDynamicField() {
        // given wrongId
        String wrongId = "dump";

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> chargeService.updateDynamicField(wrongId, new HashMap<>()))
                .isInstanceOf(NoEntityFoundException.class);


        // given name, totalCount, currentCount
        String chargePlaceId = this.charge.getChargePlaceId();
        Charge charge = this.charge.clone();

        String name = "updateTestName";
        long totalCount = 20L;
        long currentCount = 10L;

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", name);
        objectMap.put("totalCount", totalCount);
        objectMap.put("currentCount", currentCount);

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.of(charge));
        Charge result = chargeService.updateDynamicField(chargePlaceId, objectMap);

        // then
        assertThat(result.getName()).isNotEqualTo(this.charge.getName());
        assertThat(result.getTotalCount()).isNotEqualTo(this.charge.getTotalCount());
        assertThat(result.getCurrentCount()).isNotEqualTo(this.charge.getCurrentCount());

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getTotalCount()).isEqualTo(totalCount);
        assertThat(result.getCurrentCount()).isEqualTo(currentCount);
    }

    @Test
    void deleteCharge() {
        // given
        String chargePlaceId = this.charge.getChargePlaceId();

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.of(this.charge));
        String result = chargeService.deleteCharge(chargePlaceId);

        // then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Delete " + chargePlaceId + " Success");

        // when
        when(chargeRepository.findByChargePlaceId(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> chargeService.deleteCharge(chargePlaceId))
                .isInstanceOf(NoEntityFoundException.class);
    }








}