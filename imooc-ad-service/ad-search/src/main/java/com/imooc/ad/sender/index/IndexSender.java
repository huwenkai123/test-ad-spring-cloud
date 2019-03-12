package com.imooc.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.ad.dump.table.*;
import com.imooc.ad.handler.AdLevelDateHandler;
import com.imooc.ad.index.DataLevel;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.dto.MysqlRowData;
import com.imooc.ad.sender.ISender;
import com.imooc.ad.utils.CommonUnits;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("indexSender")
public class IndexSender implements ISender {
    @Override
    public void sender(MysqlRowData mysqlRowData) {
        String level = mysqlRowData.getLevel();
        if (DataLevel.LEVEL2.getLevel().equals(level)) {
            Level2RowData(mysqlRowData);
        } else if (DataLevel.LEVEL3.getLevel().equals(level)){
            Level3RowData(mysqlRowData);
        } else if (DataLevel.LEVEL4.getLevel().equals(level)) {
            Level4RowData(mysqlRowData);
        } else {
            log.error("MysqlRowDate error :{}", JSON.toJSONString(mysqlRowData));
        }
    }

    private void Level2RowData(MysqlRowData rowData) {
        if (rowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            List<AdPlaneTable> planeTables = new ArrayList<>();
            for (Map<String, String> fieldValueMap : rowData.getFieldValueMao()) {
                AdPlaneTable adPlaneTable = new AdPlaneTable();
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            adPlaneTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            adPlaneTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            adPlaneTable.setUserId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            adPlaneTable.setStartDate(CommonUnits.parseStringDate(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            adPlaneTable.setEndDate(CommonUnits.parseStringDate(v));
                            break;
                    }
                });
                planeTables.add(adPlaneTable);
            }
            planeTables.forEach(p -> AdLevelDateHandler.handleLevel2(p, rowData.getOpType()));
        } else if (rowData.getTableName().equals(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)) {
            List<AdCreateTable> createTables = new ArrayList<>();
            for (Map<String, String> filedMap : rowData.getFieldValueMao()) {
                AdCreateTable createTable = new AdCreateTable();
                filedMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            createTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            createTable.setType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            createTable.setMaterialType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            createTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            createTable.setWidth(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            createTable.setAuditStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            createTable.setAdUrl(v);
                            break;
                    }
                });
                createTables.add(createTable);
            }
            createTables.forEach(c ->
                    AdLevelDateHandler.handleLevel2(c, rowData.getOpType()));
        }
    }

    private void Level3RowData(MysqlRowData rowData) {
        if (rowData.getTableName().equals(Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitTable> unitTables = new ArrayList<>();
            for (Map<String, String> filedValueMap : rowData.getFieldValueMao()) {
                AdUnitTable unitTable = new AdUnitTable();
                filedValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                    }
                });
                unitTables.add(unitTable);
            }
            unitTables.forEach(u -> AdLevelDateHandler.handleLevel3(u, rowData.getOpType()));
        } else if (rowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for (Map<String, String> fileValue : rowData.getFieldValueMao()) {
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();
                fileValue.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            }
            creativeUnitTables.forEach(u -> AdLevelDateHandler.handleLevel3(u, rowData.getOpType()));
        }
    }

    private void Level4RowData(MysqlRowData rowData) {
        switch (rowData.getTableName()) {
            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();
                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMao()) {
                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();
                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                                districtTable.setProvince(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                        }
                    });
                    districtTables.add(districtTable);
                }
                districtTables.forEach(
                        d -> AdLevelDateHandler.handleLevel4(d, rowData.getOpType())
                );
                break;
            case Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME:
                List<AdUnitItTable> itTables = new ArrayList<>();
                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMao()) {
                    AdUnitItTable itTable = new AdUnitItTable();
                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                                itTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                                itTable.setItTag(v);
                                break;
                        }
                    });
                    itTables.add(itTable);
                }
                itTables.forEach(
                        i -> AdLevelDateHandler.handleLevel4(i, rowData.getOpType())
                );
                break;
            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:
                List<AdUnitKeywordTable> keywordTables = new ArrayList<>();
                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMao()) {
                    AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                keywordTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                keywordTable.setKeyword(v);
                                break;
                        }
                    });
                    keywordTables.add(keywordTable);
                }

                keywordTables.forEach(
                        k -> AdLevelDateHandler.handleLevel4(k, rowData.getOpType())
                );
                break;
        }
    }
}
