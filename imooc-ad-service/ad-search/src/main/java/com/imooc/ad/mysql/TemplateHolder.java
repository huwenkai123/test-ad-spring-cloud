package com.imooc.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.dto.TemPlate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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

    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = cl.getResourceAsStream(path);

        try {
            TemPlate template = JSON.parseObject(inputStream, Charset.defaultCharset(), TemPlate.class);
            this.template = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException es) {
            log.error(es.getMessage());
            throw new RuntimeException("fail");
        }
    }

    private void loadMeta() {
        for (Map.Entry<String, TableTemplate> entry : template.getTableTemplateMap().entrySet()) {
            TableTemplate table = entry.getValue();
            List<String> unpdateFiles = table.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> addFiles = table.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> delFiles = table.getOpTypeFieldSetMap().get(OpType.DELETE);

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), table.getTableName()
            }, (res, i) -> {
                int pos = res.getInt("ORDINAL_POSITION");
                String colName = res.getString("COLUMN_NAME");
                if ((null != unpdateFiles && unpdateFiles.contains(colName))
                    || (null != addFiles && addFiles.contains(colName)) || (null != delFiles && delFiles.contains(colName))) {
                    table.getPosMap().put((pos - 1), colName);
                }
                return null;
            });

        }
    }

}
