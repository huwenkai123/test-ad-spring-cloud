package com.imooc.ad.service;

import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.vo.*;

public interface IAdUnitService {
    AdUnitResponse createUnit(AdUnitRequest request) throws AdExpection;

    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdExpection;

    AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdExpection;

    AdUnitDistrictResponse createAdUnitDistrict(AdUnitDistrictRequest request) throws AdExpection;

    CreativeUnitResponse createCreativeUnit (CreativeUnitRequest request) throws AdExpection;
}
