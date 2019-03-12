package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSlot {
    //广告位的编码
    private String adSlotCode;
    //广告位的流量类型
    private Integer positionType;
    //广告位的尺寸
    private Integer weight;
    private Integer height;
    //广告物料类型
    private List<Integer> type;
    //最低出价
    private Integer minCpm;
}
