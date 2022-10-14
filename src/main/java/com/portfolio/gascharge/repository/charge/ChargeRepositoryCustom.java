package com.portfolio.gascharge.repository.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.domain.charge.search.ChargeSearchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChargeRepositoryCustom {
    Page<Charge> findChargeWithSearchStatus(ChargeSearchStatus chargeSearchStatus, Pageable pageable);
}
