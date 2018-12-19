package com.pwccn.esg;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class FlywayApp {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate() {
        //初始化flyway类
        Flyway flyway = new Flyway();
        //设置加载数据库的相关配置信息
        flyway.setDataSource(dataSource);
        //设置存放flyway metadata数据的表名，默认"schema_version"，可不写
        flyway.setTable("schema_version");
        //设置flyway扫描sql升级脚本、java升级脚本的目录路径或包路径，默认"db/migration"，可不写
        flyway.setLocations("db");
        //设置sql脚本文件的编码，默认"UTF-8"，可不写
        flyway.setEncoding("UTF-8");

        flyway.migrate();
    }


}
