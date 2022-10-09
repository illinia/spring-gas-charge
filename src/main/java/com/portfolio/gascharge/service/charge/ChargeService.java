package com.portfolio.gascharge.service.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.repository.charge.ChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChargeService {

    private final ChargeRepository chargeRepository;

    public Optional<Charge> findById(String id) {
        return chargeRepository.findById(id);
    }

    public String saveCharge(Charge charge) {
        Charge save = chargeRepository.save(charge);
        return save.getId();
    }
}
