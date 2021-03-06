package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.imooc.ad.mysql.TemplateHolder;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;
    private Map<String, Ilistener> ilistenerMap = new HashMap<>();
    private final TemplateHolder templateHolder;

    private String genKey(String dbName, String tableName) {
        return dbName + ":" + tableName;
    }

    public void register(String _dbName, String _tableName, Ilistener ilistener) {
        log.info("register: {} - {}", _dbName,_tableName );
        this.ilistenerMap.put(genKey(_dbName, _tableName), ilistener);
    }

    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();
        log.debug("event type:{}", type);
        if (type == EventType.TABLE_MAP) {
            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }

        if (type != EventType.EXT_DELETE_ROWS && type != EventType.EXT_UPDATE_ROWS && type != EventType.EXT_WRITE_ROWS) {
            return;
        }

        //
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)) {
            log.error("no meta data event");
            return;
        }

        //找出对应表有兴趣的监听器

        String key = genKey(this.dbName, this.tableName);
        Ilistener ilistener = this.ilistenerMap.get(key);
        if (null == ilistener) {
            log.debug("skip{}",key);
            return;
        }
        try {
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }
            rowData.setEventType(type);
            ilistener.onEvent(rowData);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }finally {
            this.dbName = "";
            this.tableName = "";
        }


    }

    private List<Serializable[]> getAfterValelues(EventData eventData) {
        if (eventData instanceof WriteRowsEventData) {
            return ((WriteRowsEventData) ((WriteRowsEventData) eventData)).getRows();
        }

        if (eventData instanceof UpdateRowsEventData) {
            return ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }

        if (eventData instanceof DeleteRowsEventData) {
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();
    }


    private BinlogRowData buildRowData(EventData eventData) {
        TableTemplate table = templateHolder.getTable(tableName);
        if (null == table) {
            log.warn("table:{} not found", tableName);
            return null;
        }
        List<Map<String, String>> afterMapList = new ArrayList<>();
        for (Serializable[] after : getAfterValelues(eventData)) {
            Map<String, String> afterMap = new HashMap<>();
            int colLen = after.length;
            for (int ix = 0; ix < colLen; ix++) {
                //取出当前位置对应的列名
                String colName = table.getPosMap().get(ix);
                if (StringUtils.isEmpty(colName)) {
                    log.debug("no colum {}",colName);
                    continue;
                }
                String colValue = after[ix].toString();
                afterMap.put(colName, colValue);
            }
            afterMapList.add(afterMap);
        }
        BinlogRowData binlogRowData = new BinlogRowData();
        binlogRowData.setAfter(afterMapList);
        binlogRowData.setTable(table);
        return binlogRowData;
    }
}
