package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.entity.unit_condition.CreativeUnit;
import com.imooc.ad.service.IAdUnitService;
import com.imooc.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdUnitOpController {

    private final IAdUnitService adUnitService;

    @Autowired
    public AdUnitOpController(IAdUnitService adUnitService) {
        this.adUnitService = adUnitService;
    }


    @PostMapping("/create/adUnit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest adUnitRequest) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(adUnitRequest));
        return adUnitService.createUnit(adUnitRequest);
    }

    @PostMapping("/create/adItUnit")
    public AdUnitItResponse createAdUnitIt(@RequestBody AdUnitItRequest request) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return adUnitService.createAdUnitIt(request);
    }

    @PostMapping("/create/adUnitKeyword")
    public AdUnitKeywordResponse createAdUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return adUnitService.createUnitKeyword(request);
    }

    @PostMapping("/create/adUnitDistrict")
    public AdUnitDistrictResponse createAdUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return adUnitService.createAdUnitDistrict(request);
    }

    @PostMapping("/create/createCreativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request) throws AdExpection{
        log.info("ad=-sponsor: createUser -> {}", JSON.toJSONString(request));
        return adUnitService.createCreativeUnit(request);
    }
}
