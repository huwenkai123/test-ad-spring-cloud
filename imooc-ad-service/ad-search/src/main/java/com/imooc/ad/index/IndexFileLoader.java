package com.imooc.ad.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.ad.dump.Dconstant;
import com.imooc.ad.ad.dump.table.*;
import com.imooc.ad.handler.AdLevelDateHandler;
import com.imooc.ad.mysql.constant.OpType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@DependsOn("dataTable")
public class IndexFileLoader {

    @PostConstruct
    public void init() {
        List<String> adPlanStrings = loadDumpData(String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_PLAN));
        adPlanStrings.forEach(p -> AdLevelDateHandler.handleLevel2(JSON.parseObject(p, AdPlaneTable.class), OpType.ADD));

        List<String> adCreativeStrings = loadDumpData(String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_CREATIVE));
        adCreativeStrings.forEach(p -> AdLevelDateHandler.handleLevel2(JSON.parseObject(p, AdCreateTable.class), OpType.ADD));

        List<String> adUnitStrings = loadDumpData(String.format("%s%s", Dconstant.DATA_ROOT_DIR, Dconstant.AD_UNIT));
        adUnitStrings.forEach(u -> AdLevelDateHandler.handleLevel3(JSON.parseObject(u, AdUnitTable.class), OpType.ADD));

        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s",
                        Dconstant.DATA_ROOT_DIR,
                        Dconstant.AD_CREATIVE_UNIT)
        );
        adCreativeUnitStrings.forEach(cu -> AdLevelDateHandler.handleLevel3(
                JSON.parseObject(cu, AdCreativeUnitTable.class),
                OpType.ADD
        ));

        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s",
                        Dconstant.DATA_ROOT_DIR,
                        Dconstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(d -> AdLevelDateHandler.handleLevel4(
                JSON.parseObject(d, AdUnitDistrictTable.class),
                OpType.ADD
        ));

        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s",
                        Dconstant.DATA_ROOT_DIR,
                        Dconstant.AD_UNIT_IT)
        );
        adUnitItStrings.forEach(i -> AdLevelDateHandler.handleLevel4(
                JSON.parseObject(i, AdUnitItTable.class),
                OpType.ADD
        ));

        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s",
                        Dconstant.DATA_ROOT_DIR,
                        Dconstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(k -> AdLevelDateHandler.handleLevel4(
                JSON.parseObject(k, AdUnitKeywordTable.class),
                OpType.ADD
        ));

    }

    private List<String> loadDumpData(String fileName) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName))){
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
