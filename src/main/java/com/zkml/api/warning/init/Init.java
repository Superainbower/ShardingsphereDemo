package com.zkml.api.warning.init;

import com.zkml.api.warning.service.ServiceFactory;
import com.zkml.api.warning.service.impl.clickhouse.ClickhouseAbstractServiceImpl;
import com.zkml.api.warning.service.impl.mysql.MysqlAbstractServiceImpl;
import com.zkml.api.warning.storage.StorageType;
import com.zkml.api.warning.utils.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Init implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(Init.class);

    @Autowired
    ConfigProperties configProperties;
    @Resource
    ServiceFactory serviceFactory;
    @Resource
    MysqlAbstractServiceImpl mysqlAbstractService;
    @Resource
    ClickhouseAbstractServiceImpl clickhouseAbstractService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("项目初始化开始");
        if(StorageType.valueOf(configProperties.getStorage()).equals(StorageType.MYSQL)){
            serviceFactory.setAbstractService(mysqlAbstractService);
        }else if (StorageType.valueOf(configProperties.getStorage()).equals(StorageType.CLICKHOUSE)){
//            serviceFactory.setAbstractService(clickhouseAbstractService);
        }
        log.info("项目初始化结束");
    }
}