package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@Data
public class ParseTemplate {
    private String database;

    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(TemPlate _template) {
        ParseTemplate template = new ParseTemplate();
        template.setDatabase(_template.getDatabases());
        for (JsonTable jsonTable : _template.getTableList()) {
            String name = jsonTable.getTableName();
            String level = jsonTable.getLevel().toString();
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setLevel(level);
            tableTemplate.setTableName(name);
            template.tableTemplateMap.put(name, tableTemplate);
            //遍历操作类型对应的列
            Map<OpType, List<String>> opTypeFieldSetMap = tableTemplate.getOpTypeFieldSetMap();
            for (JsonTable.Colum colum : jsonTable.getInsert()) {
                getAndCreateIfNeed(
                        OpType.ADD, opTypeFieldSetMap, ArrayList::new
                ).add(colum.getColumn());
            }
            for (JsonTable.Colum colum : jsonTable.getUpdate()) {
                getAndCreateIfNeed(
                        OpType.UPDATE, opTypeFieldSetMap, ArrayList::new
                ).add(colum.getColumn());
            }
            for (JsonTable.Colum colum : jsonTable.getDelete()) {
                getAndCreateIfNeed(
                        OpType.DELETE, opTypeFieldSetMap, ArrayList::new
                ).add(colum.getColumn());
            }
        }
        return template;
    }

    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map, Supplier<R> supplier) {
        return map.computeIfAbsent(key, k -> supplier.get());
    }


}
