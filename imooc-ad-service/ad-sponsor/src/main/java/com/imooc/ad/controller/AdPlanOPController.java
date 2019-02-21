package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.service.IAdPlanService;
import com.imooc.ad.vo.AdPlanRequest;
import com.imooc.ad.vo.AdPlanResponse;
import com.imooc.ad.vo.AdPlaneGetRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class AdPlanOPController {
    private final IAdPlanService adPlanService;

    @Autowired
    public AdPlanOPController(IAdPlanService adPlanService) {
        this.adPlanService = adPlanService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlane(@RequestBody AdPlanRequest request) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return adPlanService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanList(@RequestBody AdPlaneGetRequest request) throws AdExpection{
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return adPlanService.getAdPlaneByIds(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return adPlanService.updateAdPlan(request);
    }

    @PostMapping("/delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        adPlanService.deleteAdPlan(request);
    }
}
