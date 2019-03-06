package com.imooc.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonTable {
    private String tableName;
    private Integer level;
    private List<Colum> insert;
    private List<Colum> update;
    private List<Colum> delete;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Colum {
        private String column;
    }
}
