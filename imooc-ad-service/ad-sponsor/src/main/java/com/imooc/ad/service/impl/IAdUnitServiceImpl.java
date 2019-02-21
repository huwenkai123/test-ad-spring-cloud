package com.imooc.ad.service.impl;


import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUnitRepository;
import com.imooc.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.imooc.ad.dao.unit_condition.AdUnitItRepository;
import com.imooc.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.imooc.ad.dao.unit_condition.CreativeUnitRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import com.imooc.ad.entity.unit_condition.AdUnitIt;
import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import com.imooc.ad.service.IAdUnitService;
import com.imooc.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IAdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository planRepository;
    private AdUnitRepository unitRepository;
    private AdUnitKeywordRepository unitKeywordRepository;
    private AdUnitItRepository unitItRepository;
    private AdUnitDistrictRepository unitDistrictRepository;
    private CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public IAdUnitServiceImpl(AdPlanRepository planRepository, AdUnitRepository unitRepository, AdUnitKeywordRepository unitKeywordRepository, AdUnitItRepository unitItRepository, AdUnitDistrictRepository unitDistrictRepository, CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitItRepository = unitItRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdExpection {
        if (!request.createValidate()) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdPlan> adPlan =
                planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdExpection(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(), request.getUnitName()
        );
        if (oldAdUnit != null) {
            throw new AdExpection(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newAdUnit = unitRepository.save(
                new AdUnit(request.getPlanId(), request.getUnitName(),
                        request.getPositionType(), request.getBudget())
        );

        return new AdUnitResponse(newAdUnit.getId(),
                newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdExpection {
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword :: getUnitId).collect(Collectors.toList());
        if (!isRelateUnitExist(unitIds)) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(i -> unitKeywords.add(new AdUnitKeyword(i.getUnitId(), i.getKeyWord())));
        ids = unitKeywordRepository.saveAll(unitKeywords).stream()
                .map(AdUnitKeyword::getId)
                .collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdExpection {
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt :: getUnitId).collect(Collectors.toList());
        if (!isRelateUnitExist(unitIds)) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> unitIts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitIts())) {
            request.getUnitIts().forEach(i -> unitIts.add(new AdUnitIt(i.getUnitId(), i.getItTag())));
            ids = unitItRepository.saveAll(unitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());
        }
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createAdUnitDistrict(AdUnitDistrictRequest request) throws AdExpection {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if (!isRelateUnitExist(unitIds)) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        request.getUnitDistricts().forEach(d -> unitDistricts.add(
                new AdUnitDistrict(d.getUnitId(), d.getProvince(),
                        d.getCity())
        ));
        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) {
        return null;
    }

    private boolean isRelateUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }
        return unitItRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }
    private boolean isRelatedCreativeExist(List<Long> creativeIds) {
        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }
        return creativeUnitRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
}
