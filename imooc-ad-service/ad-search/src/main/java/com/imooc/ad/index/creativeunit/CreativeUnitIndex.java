package com.imooc.ad.index.creativeunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Component
@Slf4j
public class CreativeUnitIndex implements IndexAware<String, CreativeUnitObject> {

    private static Map<String, CreativeUnitObject> objectMap;
    //adId unitId
    private static Map<Long, Set<Long>> createUnitMap;

    private static Map<Long, Set<Long>> unitCreativeMap;


    static {
        objectMap = new ConcurrentHashMap<>();
        createUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("before add {}", objectMap);
        objectMap.put(key, value);
        Set<Long> unitSet = createUnitMap.get(value.getAdId());
        if (CollectionUtils.isEmpty(unitSet)) {
            unitSet = new ConcurrentSkipListSet<>();
            createUnitMap.put(value.getAdId(), unitSet);
        }
        unitSet.add(value.getUnitId());
        Set<Long> creative = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isEmpty(creative)) {
            creative = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(), creative);
        }
        creative.add(value.getAdId());
        log.info("after add {}", objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("creativeUnitIndex not support");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        objectMap.remove(key);

        Set<Long> unitSet = createUnitMap.get(value.getAdId());
        if (CollectionUtils.isNotEmpty(unitSet)) {
            unitSet.remove(value.getUnitId());
        }

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isNotEmpty(creativeSet)) {
            creativeSet.remove(value.getAdId());
        }

    }
}
