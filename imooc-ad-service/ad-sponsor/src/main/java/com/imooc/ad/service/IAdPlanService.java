package com.imooc.ad.service;

import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.vo.AdPlanRequest;
import com.imooc.ad.vo.AdPlanResponse;
import com.imooc.ad.vo.AdPlaneGetRequest;

import java.util.List;

public interface IAdPlanService {
    AdPlanResponse createAdPlan(AdPlanRequest planRequest) throws AdExpection;

    List<AdPlan> getAdPlaneByIds(AdPlaneGetRequest request) throws AdExpection;

    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdExpection;

    void deleteAdPlan(AdPlanRequest request) throws AdExpection;

}
