package com.portfolio.gascharge.batch.api;

import com.portfolio.gascharge.domain.charge.Charge;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChargeApi {
    public ResponseEntity<String> getChargeResponseEntity() {
        System.out.println("getChargeResponseEntity");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "jEtGJFDv55eQsBUN22VQro7093O2BQJlG6C8J1sVI77xYq0gA6My2MJ0hfK69qT5");

        URI uri = URI.create("http://el.h2nbiz.or.kr/api/chrstnList/currentInfo");

        return restTemplate.exchange(new RequestEntity<String>(headers, HttpMethod.GET, uri), String.class);
    }

    public List<ChargeApiDto> getChargeDtoList(ResponseEntity<String> res) throws ParseException {
        System.out.println("getChargeDtoList");
        List<ChargeApiDto> charges = new ArrayList<>();

        JSONArray array = (JSONArray) new JSONParser().parse(res.getBody());

        for (Object o : array) {
            JSONObject object = (JSONObject) o;

            ChargeApiDto charge = ChargeApiDto.builder()
                    .id((String) object.get("chrstn_mno"))
                    .name((String) object.get("chrstn_nm"))
                    .totalCount((Long) object.get("prfect_elctc_posbl_alge"))
                    .currentCount((Long) object.get("wait_vhcle_alge"))
                    .build();

            charges.add(charge);
        }

        return charges;
    }

    public List<Charge> getChargeList(List<ChargeApiDto> list) {
        return list.stream().map(item -> item.toEntity()).collect(Collectors.toList());
    }
}
