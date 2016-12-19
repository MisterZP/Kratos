package com.kratos.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by zengping on 2016/12/16.
 */
@WebServlet(name = "druidServlet",
        urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "resetEnable", value = "true"),
                @WebInitParam(name = "loginUsername", value = "druid"),
                @WebInitParam(name = "loginPassword", value = "druid"),
                @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.jsp,/druid/**//*")
        })
public class DruidServlet extends StatViewServlet {
}
