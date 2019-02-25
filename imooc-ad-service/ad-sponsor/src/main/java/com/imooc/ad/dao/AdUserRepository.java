package com.imooc.ad.dao;

import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {
    AdUser findByUsername(String username);


}
