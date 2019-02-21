package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdUnitDistrictRequest {


    private List<UnitDistrict> unitDistricts;
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UnitDistrict {
        private Long unitId;
        private String province;
        private String city;
    }
}
