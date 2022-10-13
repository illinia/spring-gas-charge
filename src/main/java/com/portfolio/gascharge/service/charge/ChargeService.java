package com.portfolio.gascharge.service.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeStatus;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.error.exception.jpa.NoEntityFoundException;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import com.portfolio.gascharge.utils.entity.EntityDynamicUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChargeService {

    private final ChargeRepository chargeRepository;

    public void updateCount(String chargePlaceId, Long totalCount, Long currentCount) {
        Optional<Charge> byChargePlaceId = chargeRepository.findByChargePlaceId(chargePlaceId);

        if (byChargePlaceId.isEmpty()) {
            throw new NoEntityFoundException();
        }

        Charge charge = byChargePlaceId.get();

        charge.updateCounts(totalCount, currentCount);
    }

    @Transactional(readOnly = true)
    public Charge findByChargePlaceId(String chargePlaceId) {
        Optional<Charge> byChargePlaceId = chargeRepository.findByChargePlaceId(chargePlaceId);

        if (byChargePlaceId.isEmpty()) {
            throw new NoEntityFoundException(Charge.class, chargePlaceId);
        }

        return byChargePlaceId.get();
    }

    public Charge saveCharge(Charge charge) {
        Optional<Charge> byId = chargeRepository.findByChargePlaceId(charge.getChargePlaceId());

        if (byId.isPresent()) {
            throw new DuplicateKeyException("ChargePlaceId is duplicated. Duplicated chargePlaceId is " + charge.getChargePlaceId());
        } else {
            return chargeRepository.save(charge);
        }
    }

    @Transactional(readOnly = true)
    public Page<Charge> findAll(Pageable pageable) {
        return chargeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Charge> findAll(String name, ChargePlaceMembership membership, Pageable pageable) {
        ChargeStatus chargeStatus = new ChargeStatus();
        chargeStatus.setName(name);
        chargeStatus.setChargePlaceMembership(membership);

        return chargeRepository.findChargeWithSearchStatus(chargeStatus, pageable);
    }

    public Charge updateDynamicField(String chargePlaceId, Map<String, Object> attributesMap) {
        Optional<Charge> byChargePlaceId = chargeRepository.findByChargePlaceId(chargePlaceId);

        if (byChargePlaceId.isEmpty()) {
            throw new NoEntityFoundException(Charge.class, chargePlaceId);
        }

        Charge charge = byChargePlaceId.get();

        EntityDynamicUpdater.update(attributesMap, charge);

        return charge;
    }

    public String deleteCharge(String chargePlaceId) {
        Optional<Charge> byChargePlaceId = chargeRepository.findByChargePlaceId(chargePlaceId);

        if (byChargePlaceId.isEmpty()) {
            throw new NoEntityFoundException(Charge.class, chargePlaceId);
        }

        Charge charge = byChargePlaceId.get();

        chargeRepository.delete(charge);

        return "Delete " + chargePlaceId + " Success";
    }
}
