package com.imooc.ad.index.district;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.utils.CommonUnits;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {
    private static Map<String, Set<Long>> districtUnitMap;
    private static Map<Long, Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("DistrictKeywordIndex, before: {}", districtUnitMap);
        Set<Long> unitIdSet = CommonUnits.getorCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIdSet.addAll(value);
        for (Long unitId : value) {
            Set<String> districts = CommonUnits.getorCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new );
            districts.add(key);
        }
        log.info("DistrictKeywordIndex, after: {}", districtUnitMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.info("district not support");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("DistrictKeywordIndex, before: {}", districtUnitMap);
        Set<Long> unitIds = CommonUnits.getorCreate(key, districtUnitMap, ConcurrentSkipListSet::new );
        unitIds.removeAll(value);
        for (Long unitId : value) {
            Set<String> kewords =  CommonUnits.getorCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            kewords.remove(key);
        }
        log.info("DistrictKeywordIndex, after: {}", districtUnitMap);
    }

    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts) {
        if (unitDistrictMap.containsKey(adUnitId) && CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))) {
            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);
            List districesList = districts.stream().map(a -> CommonUnits.stringConcat(a.getProvince(), a.getCity())).collect(Collectors.toList());
            return CollectionUtils.isSubCollection(districesList, unitDistricts);
        }
        return false;

    }
}
