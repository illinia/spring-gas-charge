package com.portfolio.gascharge.service.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeStatus;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChargeService {

    private final ChargeRepository chargeRepository;

    public Optional<Charge> findById(String id) {
        return chargeRepository.findById(id);
    }

    public Charge saveCharge(Charge charge) {
        Optional<Charge> byId = chargeRepository.findById(charge.getId());

        if (byId.isPresent()) {
            throw new DuplicateKeyException("Charge id is duplicated. id is " + charge.getId());
        } else {
            return chargeRepository.save(charge);
        }
    }

    public Page<Charge> findAll(Pageable pageable) {
        return chargeRepository.findAll(pageable);
    }

    public Page<Charge> findAll(String name, ChargePlaceMembership membership, Pageable pageable) {
        ChargeStatus chargeStatus = new ChargeStatus();
        chargeStatus.setName(name);
        chargeStatus.setChargePlaceMembership(membership);

        System.out.println("findAll" + chargeStatus);
        return chargeRepository.findChargeWithSearchStatus(chargeStatus, pageable);
    }


}
