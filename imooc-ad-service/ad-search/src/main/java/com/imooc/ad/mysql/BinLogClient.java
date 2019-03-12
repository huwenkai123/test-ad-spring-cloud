package com.imooc.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.imooc.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Component
public class BinLogClient {
    private BinaryLogClient client;

    private final BinLogConfig config;

    private final AggregationListener listener;


    @Autowired
    public BinLogClient(AggregationListener listener, BinLogConfig config) {
        this.listener = listener;
        this.config = config;
    }

    public void connect() {
        new Thread(() -> {
            client = new BinaryLogClient(config.getHost(), Integer.valueOf(config.getPort()), config.getUsername(), config.getPassword());
            if (!StringUtils.isEmpty(config.getBinlogName()) && !config.getPosition().equals(-1L)) {
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(listener);
            try {
                log.info("connect to mysql start");
                client.connect();
                log.info("connect to mysql start");
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void close() {
        try {
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

