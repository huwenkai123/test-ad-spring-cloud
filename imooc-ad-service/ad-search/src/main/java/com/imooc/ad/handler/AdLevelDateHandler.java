package com.imooc.ad.handler;


import com.imooc.ad.ad.dump.table.*;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adUnit.AdUnitIndex;
import com.imooc.ad.index.adUnit.AdUnitObject;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.creativeunit.CreativeUnitObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.utils.CommonUnits;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
*1.索引之间存在层级的划分，也就是依赖关系的划分
* 2.加载全量索引其实是增量索引 “添加”的一种特殊实现
* */
@Slf4j
public class AdLevelDateHandler {

    public static  void handleLevel2(AdPlaneTable planeTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(planeTable.getId(),planeTable.getUserId(), planeTable.getPlanStatus(), planeTable.getStartDate(), planeTable.getEndDate());
        handleBinlogEvent(DataTable.of(AdPlanIndex.class), planObject.getPlanId(), planObject, type);
    }

    public static void handleLevel2(AdCreateTable createTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(
                createTable.getAdId(),
                createTable.getName(),
                createTable.getType(),
                createTable.getMaterialType(),
                createTable.getHeight(),
                createTable.getWidth(),
                createTable.getAuditStatus(),
                createTable.getAdUrl()
        );
        handleBinlogEvent(DataTable.of(CreativeIndex.class), creativeObject.getAdId(), creativeObject, type);
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (null == adPlanObject) {
            log.error("handleLevel3 found AdPlanObject error {}", unitTable.getPlanId());
            return;
        }

        AdUnitObject unitObject = new AdUnitObject(unitTable.getUnitId(), unitTable.getUnitStatus(), unitTable.getPositionType(), unitTable.getPlanId(), adPlanObject);
        handleBinlogEvent(DataTable.of(AdUnitIndex.class), unitTable.getUnitId(), unitObject, type);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        if (creativeObject != null || unitObject != null) {
            log.error("handleLevel3 fond error");
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(), creativeUnitTable.getUnitId());

        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class), CommonUnits.stringConcat(creativeUnitObject.getAdId().toString(),creativeUnitObject.getUnitId().toString()), creativeUnitObject, type);
    }


    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("not found district");
            return;
        }

        String key = CommonUnits.stringConcat(unitDistrictTable.getProvince(), unitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class), key, value, type);
    }


    public static void handleLevel4(AdUnitItTable unitItTable, OpType opType) {
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("not found district");
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitItIndex.class), unitItTable.getItTag(), value, opType);
    }

    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType opType) {
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if (unitObject == null) {
            log.error("not found district");
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitKeywordTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitItIndex.class), unitKeywordTable.getKeyword(), value, opType);
    }



    private static <K, V> void handleBinlogEvent(IndexAware<K, V> index,
                                                 K key, V value,
                                                 OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
        }
    }
}
