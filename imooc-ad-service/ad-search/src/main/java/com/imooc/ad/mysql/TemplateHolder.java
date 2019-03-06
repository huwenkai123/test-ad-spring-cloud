package com.imooc.ad.mysql;

import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate template;

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_SCHEMA = "select table_schema, table_name, column_name, ordinal_position from information_schema.columns where table_schema = ? and table_name = ?";

    private void loadMeta() {
        for (Map.Entry<String, TableTemplate> entry : template.getTableTemplateMap().entrySet()) {
            TableTemplate table = entry.getValue();
            List<String> unpdateFiles = table.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> addFiles = table.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> delFiles = table.getOpTypeFieldSetMap().get(OpType.DELETE);

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), table.getTableName()
            }, (res, i) -> {
                return null;
            });

        }
    }

}
