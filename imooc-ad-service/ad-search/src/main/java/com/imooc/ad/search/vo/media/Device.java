package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    //设备
    private String deviceCode;

    private String mac;

    private String ip;

    private String model;

    private String displaySize;

    private String screenSize;
    //设备序列号
    private String serialName;
}
