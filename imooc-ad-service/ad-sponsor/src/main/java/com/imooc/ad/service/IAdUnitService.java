package com.imooc.ad.service;

import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.vo.AdUnitRequest;
import com.imooc.ad.vo.AdUnitResponse;

public interface IAdUnitService {
    AdUnitResponse createUnit(AdUnitRequest request) throws AdExpection;

}
