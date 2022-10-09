package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.controller.charge.dto.ChargeResponse;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.service.charge.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChargeController {

    private final ChargeService service;

    @GetMapping("/charge/{id}")
    public ResponseEntity findById(@PathVariable String id) {
        Optional<Charge> byId = service.findById(id);

        if (byId.isEmpty()) {
            return new ResponseEntity(null,null, HttpStatus.NOT_FOUND);
        }

        Charge charge = byId.get();

        ChargeResponse chargeResponse = ChargeResponse.builder()
                .id(charge.getId())
                .name(charge.getName())
                .totalCount(charge.getTotalCount())
                .currentCount(charge.getCurrentCount())
                .build();

        return new ResponseEntity<>(chargeResponse, null, HttpStatus.OK);
    }
}
