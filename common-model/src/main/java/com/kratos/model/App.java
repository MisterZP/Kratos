package com.kratos.model;

import lombok.Data;
import tk.mybatis.mapper.entity.IDynamicTableName;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Data
public class App implements IDynamicTableName {
    @Transient//非表字段，表名
    private String dynamicTableName = "App";
    @Id
    @Column(name = "appId")
    private Long appId;
    @Column(name = "appKey")
    private String appKey;
    @Column(name = "secretKey")
    private String secretKey;
    @Column(name = "sku")
    private String sku;
    @Column(name = "sign")
    private String sign;
    @Column(name = "groupId")
    private Long groupId;
    @Column(name = "online")
    private Integer online;
    @Column(name = "gameBid")
    private String gameBid;
    @Column(name = "auditReport")
    private String auditReport;
    @Column(name = "updateTime")
    private Timestamp updateTime;
    @Column(name = "onlineTime")
    private Timestamp onlineTime;
    @Column(name = "createTime")
    private Timestamp createTime;
}
