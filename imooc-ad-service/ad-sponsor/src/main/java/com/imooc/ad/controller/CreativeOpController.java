package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.service.ICreativeService;
import com.imooc.ad.vo.CreativeRequest;
import com.imooc.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CreativeOpController {
    private final ICreativeService creativeService;

    @Autowired
    public CreativeOpController(ICreativeService creativeService) {
        this.creativeService = creativeService;
    }

    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest creativeRequest) throws AdExpection {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(creativeRequest));
        return creativeService.createCreative(creativeRequest);
    }
}
