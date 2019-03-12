package com.imooc.ad.search.vo;

import com.imooc.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative {
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        //展示检测
        private List<String> showMonitorUrl = Arrays.asList("www.imooc.com", "www.imooc.cn");
        //点击检测
        private List<String> clickMonitorUrl = Arrays.asList("www.imooc.com", "www.imooc.cn");
    }


    public static Creative convince(CreativeObject creativeObject) {
        Creative creative = new Creative();
        creative.setAdId(creativeObject.getAdId());
        creative.setAdUrl(creativeObject.getAdUrl());
        creative.setHeight(creativeObject.getHeight());
        creative.setWidth(creativeObject.getWidth());
        creative.setType(creativeObject.getType());
        creative.setMaterialType(creativeObject.getMaterialType());
        return creative;
    }
}
