package com.kratos.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Table(name="App")
@Document(indexName = "app", type = "app", shards = 1, replicas = 0)
public class App{
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
