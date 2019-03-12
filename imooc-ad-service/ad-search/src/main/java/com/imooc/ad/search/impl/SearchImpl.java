package com.imooc.ad.search.impl;

import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adUnit.AdUnitIndex;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.Itfeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
        //请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        //三个
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        Itfeature itfeature = request.getFeatureInfo().getItfeature();
        FeatureRelation featureRelation = request.getFeatureInfo().getFeatureRelation();
        //构造相应对象
        SearchResponse searchResponse = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = searchResponse.getAdSlot2Ads();
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;
            //根据流量类型获取初始的 AdUnit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

        }

        return null;
    }
}
