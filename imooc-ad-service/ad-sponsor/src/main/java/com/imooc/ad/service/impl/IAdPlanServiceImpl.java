package com.imooc.ad.service.impl;

import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.constant.CommonStatus;
import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUserRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUser;
import com.imooc.ad.service.IAdPlanService;
import com.imooc.ad.utils.CommonUtils;
import com.imooc.ad.vo.AdPlanRequest;
import com.imooc.ad.vo.AdPlanResponse;
import com.imooc.ad.vo.AdPlaneGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IAdPlanServiceImpl implements IAdPlanService {
    @Autowired
    private final AdUserRepository adUserRepository;
    private final AdPlanRepository adPlanRepository;

    public IAdPlanServiceImpl(AdUserRepository adUserRepository, AdPlanRepository adPlanRepository) {
        this.adUserRepository = adUserRepository;
        this.adPlanRepository = adPlanRepository;
    }


    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest planRequest) throws AdExpection {
        if (!planRequest.createValidate()) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdUser> adUser = adUserRepository.findById(planRequest.getUserId());
        if (!adUser.isPresent()) {
            throw new AdExpection(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        AdPlan oldPlan = adPlanRepository.findByUserIdAndPlanName(planRequest.getUserId(), planRequest.getPlanName());
        if (oldPlan != null) {
            throw new AdExpection(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }
        AdPlan adPlan = adPlanRepository.save(new AdPlan(planRequest.getUserId(), planRequest.getPlanName(),
                CommonUtils.parseStringDate(planRequest.getStartDate()), CommonUtils.parseStringDate(planRequest.getEndDate())));
        return new AdPlanResponse(adPlan.getId(),adPlan.getPlanName());
    }

    @Override
    @Transactional
    public List<AdPlan> getAdPlaneByIds(AdPlaneGetRequest request) throws AdExpection {
        if (!request.vaildate()) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        return adPlanRepository.findAllByIdInAndUserId(
                request.getIds(), request.getUserId()
        );
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdExpection {
        if (!request.updateValidate()) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = adPlanRepository.findByIdAndUserId(
                request.getId(), request.getUserId()
        );
        if (plan == null) {
            throw new AdExpection(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        if (request.getPlanName() != null) {
            plan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null) {
            plan.setStartDate(
                    CommonUtils.parseStringDate(request.getStartDate())
            );
        }
        if (request.getEndDate() != null) {
            plan.setEndDate(
                    CommonUtils.parseStringDate(request.getEndDate())
            );
        }

        plan.setUpdateTime(new Date());
        plan = adPlanRepository.save(plan);

        return new AdPlanResponse(plan.getId(), plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdExpection {
        if (!request.deleteValidate()) {
            throw new AdExpection(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = adPlanRepository.findByIdAndUserId(
                request.getId(), request.getUserId()
        );
        if (plan == null) {
            throw new AdExpection(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        adPlanRepository.save(plan);
    }
}
