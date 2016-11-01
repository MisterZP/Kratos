package com.kratos.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table(name = "App")
public class App implements Serializable{
	private transient static final long serialVersionUID = -2129911785381753227L;
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
