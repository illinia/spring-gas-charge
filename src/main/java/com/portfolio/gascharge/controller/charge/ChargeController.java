package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.controller.charge.dto.ChargeResponse;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.service.charge.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService service;

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable String id) {
        Optional<Charge> byId = service.findById(id);

        if (byId.isEmpty()) {
            return new ResponseEntity(null,null, HttpStatus.NOT_FOUND);
        }

        Charge charge = byId.get();

        ChargeResponse chargeResponse = ChargeResponse.toResponseDto(charge);

        return new ResponseEntity<>(chargeResponse, null, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity getChargeList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "is-membership", required = false) String isMembership,
            Pageable pageable
    ) {
        log.info(pageable + name + isMembership);

        Page<Charge> all = null;

        if (name == null && isMembership == null && pageable == null) {
            all = service.findAll(null);
        } else {
            all = service.findAll(name, ChargePlaceMembership.getChargePlaceMembership(isMembership), pageable);
        }

//        List<ChargeResponse> chargeResponseList = all.stream().map(c -> ChargeResponse.toResponseDto(c)).collect(Collectors.toList());

        return new ResponseEntity<>(all, null, HttpStatus.OK);
    }
}
