package com.imooc.ad.dao;

import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {
    AdUser findByUsername(String username);

    List<AdPlan> findAllByIdInAndUserId(List<Long> ids, Long userId);

    AdPlan findByUserIdAndPlanName(Long userId, String planName);

    List<AdPlan> findAllByPlanStatus(Integer status);
}
