package com.portfolio.gascharge.repository.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChargeRepositoryCustom {
    Page<Charge> findChargeWithSearchStatus(ChargeStatus chargeStatus, Pageable pageable);
}
