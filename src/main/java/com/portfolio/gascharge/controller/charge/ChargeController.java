package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.controller.charge.dto.PatchChargeRequestDto;
import com.portfolio.gascharge.controller.charge.dto.PostChargeRequestDto;
import com.portfolio.gascharge.controller.charge.dto.SearchChargeResponseDto;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.service.charge.ChargeService;
import com.portfolio.gascharge.utils.web.DtoFieldSpreader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService chargeService;

    @GetMapping("/{id}")
    public ResponseEntity getChargeByChargePlaceId(@PathVariable String id) {
        Charge charge = chargeService.findByChargePlaceId(id);

        SearchChargeResponseDto searchChargeResponseDto = SearchChargeResponseDto.toResponseDto(charge);

        return new ResponseEntity<>(searchChargeResponseDto, null, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity getChargeList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "is-membership", required = false) String isMembership,
            Pageable pageable) {
        log.info("getChargeList request : pageable = {}, name = {}, isMembership = {}", pageable, name, isMembership);

        List<SearchChargeResponseDto> collect = chargeService.findAll(name, ChargePlaceMembership.getChargePlaceMembership(isMembership), pageable)
                .getContent().stream().map(SearchChargeResponseDto::toResponseDto).collect(Collectors.toList());

        Page<SearchChargeResponseDto> result = new PageImpl<>(collect, pageable, collect.size());

        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addCharge(
            @RequestBody PostChargeRequestDto postChargeRequestDto) {
        Charge charge = chargeService.saveCharge(postChargeRequestDto.toEntity());
        return new ResponseEntity(SearchChargeResponseDto.toResponseDto(charge), null, HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateCharge(
            @RequestBody PatchChargeRequestDto patchChargeRequestDto) {
        Map<String, Object> attributesMap = DtoFieldSpreader.of(patchChargeRequestDto);

        Charge charge = chargeService.updateDynamicField(patchChargeRequestDto.getChargePlaceId(), attributesMap);

        return new ResponseEntity(SearchChargeResponseDto.toResponseDto(charge), null, HttpStatus.OK);
    }

    @DeleteMapping("/{chargePlaceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCharge(
            @PathVariable String chargePlaceId) {
        return chargeService.deleteCharge(chargePlaceId);
    }
}
