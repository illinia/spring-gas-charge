package com.portfolio.gascharge.repository.charge;

import com.portfolio.gascharge.domain.charge.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChargeRepository extends JpaRepository<Charge, Long>, ChargeRepositoryCustom {

    @Query("select c from Charge c where c.chargePlaceId = :chargePlaceId")
    Optional<Charge> findByChargePlaceId(@Param("chargePlaceId") String id);

    @Query("select c from Charge c where c.name like %:name%")
    List<Charge> findByName(@Param("name") String name);
}
