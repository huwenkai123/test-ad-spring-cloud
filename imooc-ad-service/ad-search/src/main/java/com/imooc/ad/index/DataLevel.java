package com.imooc.ad.index;

import lombok.Getter;

@Getter
public enum  DataLevel {
    LEVEL2("2", "LEVEL2"),
    LEVEL3("3", "LEVEL3"),
    LEVEL4("4", "LEVEL4");

    private String level;
    private String desc;
    DataLevel(String level, String desc) {
        this.level = level;
        this.desc = desc;
    }
}
