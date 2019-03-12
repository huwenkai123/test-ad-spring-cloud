package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.MysqlRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements Ilistener {

    private final AggregationListener aggregationListener;

    @Resource(name = "indexSender")
    private ISender sender;
    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    /*
    * 表的注册
    * */
    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db");
        Constant.table2Db.forEach((k, v) -> {
            aggregationListener.register(v, k, this);
        });
    }

    @Override
    public void onEvent(BinlogRowData eventData) {
        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();
        //包装城最后需要投递的数据
        MysqlRowData rowData = new MysqlRowData();
        rowData.setTableName(table.getTableName());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);
        rowData.setLevel(table.getLevel());
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
        if (fieldList == null) {
            log.warn("{} not support for {}", opType, table.getTableName());
            return;
        }
        for (Map<String, String> afterMap : eventData.getAfter()) {
            Map<String, String> _afterMap = new HashMap<>();
            for (Map.Entry<String, String> entry : _afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();
                _afterMap.put(colName, colValue);
            }
            rowData.getFieldValueMao().add(_afterMap);
        }
        sender.sender(rowData);
    }
}
