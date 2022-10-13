package com.portfolio.gascharge.controller.charge;

import com.portfolio.gascharge.controller.charge.dto.AddChargeRequestDto;
import com.portfolio.gascharge.controller.charge.dto.SearchChargeResponseDto;
import com.portfolio.gascharge.domain.charge.Charge;
import com.portfolio.gascharge.enums.charge.ChargePlaceMembership;
import com.portfolio.gascharge.service.charge.ChargeService;
import com.portfolio.gascharge.utils.web.DtoFieldSpreader;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService chargeService;

    @ApiOperation(
            value = "충전소 단건 검색", notes = "ChargePlaceId 로 충전소 단건 검색한다."
    )
    @ApiImplicitParam(
            name = "chargePlaceId",
            value = "충전소 고유값",
            required = true,
            paramType = "path",
            defaultValue = "1150020121HS2022025"
    )
    @GetMapping("/{chargePlaceId}")
    public ResponseEntity getChargeByChargePlaceId(@PathVariable @NotBlank String chargePlaceId) {
        Charge charge = chargeService.findByChargePlaceId(chargePlaceId);

        SearchChargeResponseDto searchChargeResponseDto = SearchChargeResponseDto.toResponseDto(charge);

        return new ResponseEntity<>(searchChargeResponseDto, null, HttpStatus.OK);
    }

    @ApiOperation(
            value = "충전소 페이징, 정렬 검색", notes = "충전소 이름(포함), 가맹점 여부, Pageable 객체 이용한 조회입니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                  name = "is-membership"
                  , value = "가맹점 여부"
                  , defaultValue = "not-membership"
            )
            ,@ApiImplicitParam(
                    name = "name"
                    ,value = "충전소 이름"
                    ,defaultValue = "인천"
            )
    })
    @GetMapping("")
    public ResponseEntity getChargeList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "is-membership", required = false) String isMembership,
            @NotNull Pageable pageable) {
        log.info("getChargeList request : pageable = {}, name = {}, isMembership = {}", pageable, name, isMembership);

        List<SearchChargeResponseDto> collect = chargeService.findAll(name, ChargePlaceMembership.getChargePlaceMembership(isMembership), pageable)
                .getContent().stream().map(SearchChargeResponseDto::toResponseDto).collect(Collectors.toList());

        Page<SearchChargeResponseDto> result = new PageImpl<>(collect, pageable, collect.size());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addCharge(
            @RequestBody @Valid AddChargeRequestDto addChargeRequestDto) {
        Charge charge = chargeService.saveCharge(addChargeRequestDto.toEntity());
        return new ResponseEntity(SearchChargeResponseDto.toResponseDto(charge), HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateCharge(
            @RequestBody @Valid AddChargeRequestDto updateChargeRequestDto) {
        Map<String, Object> attributesMap = DtoFieldSpreader.of(updateChargeRequestDto);

        Charge charge = chargeService.updateDynamicField(updateChargeRequestDto.getChargePlaceId(), attributesMap);

        return new ResponseEntity(SearchChargeResponseDto.toResponseDto(charge), HttpStatus.OK);
    }

    @ApiImplicitParam(
            name = "chargePlaceId",
            value = "충전소 고유값",
            required = true,
            paramType = "path",
            defaultValue = "testId"
    )
    @DeleteMapping("/{chargePlaceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteCharge(
            @PathVariable @NotBlank String chargePlaceId) {
        String result = chargeService.deleteCharge(chargePlaceId);

        return new ResponseEntity(result, HttpStatus.NO_CONTENT);
    }
}
