package com.imooc.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemPlate {
    private String databases;
    private List<JsonTable> tableList;
}
