package com.kratos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
  Created by zengping on 2016/11/1.
 */
@SpringBootApplication
public class ClientApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        LOGGER.info("" +
                "\r\n启动成功：\r\n" +
                "                             _ooOoo_\r\n" +
                "                            o8888888o\r\n" +
                "                            88\" . \"88\r\n" +
                "                            (| -_- |)\r\n" +
                "                             O\\ = /O\r\n" +
                "                         ____/`---'\\____\r\n" +
                "                       .   ' \\\\| |// `.\r\n" +
                "                        / \\\\||| : |||// \\\r\n" +
                "                      / _||||| -:- |||||- \\\r\n" +
                "                        | | \\\\\\ - /// | |\r\n" +
                "                      | \\_| ''\\---/'' | |\r\n" +
                "                       \\ .-\\__ `-` ___/-. /\r\n" +
                "                    ___`. .' /--.--\\ `. . __\r\n" +
                "                 .\"\" '< `.___\\_<|>_/___.' >'\"\".\r\n" +
                "                | | : `- \\`.;`\\ _ /`;.`/ - ` : | |\r\n" +
                "                  \\ \\ `-. \\_ __\\ /__ _/ .-` / /\r\n" +
                "          ======`-.____`-.___\\_____/___.-`____.-'======\r\n" +
                "                             `=---='\r\n" +
                "          .............................................\r\n" +
                "               佛曰：\r\n" +
                "                    爱老婆，爱生活。\r\n");
    }
}
