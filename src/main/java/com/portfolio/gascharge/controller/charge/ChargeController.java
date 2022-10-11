package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.controller.charge.dto.PostChargeRequestDto;
import com.portfolio.gascharge.controller.charge.dto.SearchChargeResponseDto;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.error.errorcode.CommonErrorCode;
import com.portfolio.gascharge.error.exception.web.RestApiException;
import com.portfolio.gascharge.service.charge.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService chargeService;

    @GetMapping("/{id}")
    public ResponseEntity getChargeByChargePlaceId(@PathVariable String id) {
        Optional<Charge> byId = chargeService.findByChargePlaceId(id);

        if (byId.isEmpty()) {
            throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }

        Charge charge = byId.get();

        SearchChargeResponseDto searchChargeResponseDto = SearchChargeResponseDto.toResponseDto(charge);

        return new ResponseEntity<>(searchChargeResponseDto, null, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity getChargeList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "is-membership", required = false) String isMembership,
            Pageable pageable
    ) {
        log.info("getChargeList request : pageable = {}, name = {}, isMembership = {}",pageable, name, isMembership);

        Page<Charge> all = null;

        if (name == null && isMembership == null && pageable == null) {
            all = chargeService.findAll(null);
        } else {
            all = chargeService.findAll(name, ChargePlaceMembership.getChargePlaceMembership(isMembership), pageable);
        }

        List<SearchChargeResponseDto> collect = all.getContent().stream().map(c -> SearchChargeResponseDto.toResponseDto(c)).collect(Collectors.toList());

        Page<SearchChargeResponseDto> result = new PageImpl<>(collect, pageable, collect.size());

        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity addCharge(
            @RequestBody PostChargeRequestDto request) {
        try {
            Charge charge = chargeService.saveCharge(request.toEntity());
            return new ResponseEntity(SearchChargeResponseDto.toResponseDto(charge), null, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            log.error("addCharge method has error about duplicated id when create new charge entity : ", e);
            throw new RestApiException(CommonErrorCode.RESOURCE_CONFLICT);
        } catch (Exception e) {
            log.error("addCharge method has error : ", e);
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
