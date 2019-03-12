package com.imooc.ad.sender;

import com.imooc.ad.mysql.dto.MysqlRowData;

public interface ISender {
    void sender(MysqlRowData mysqlRowData);
}
