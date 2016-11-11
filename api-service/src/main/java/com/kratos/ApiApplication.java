package com.kratos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by zengping on 2016/11/1.
 */
@SpringBootApplication
public class ApiApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        LOGGER.info("\n启动成功：\n" +
                "                             _ooOoo_\n" +
                "                            o8888888o\n" +
                "                            88\" . \"88\n" +
                "                            (| -_- |)\n" +
                "                             O\\ = /O\n" +
                "                         ____/`---'\\____\n" +
                "                       .   ' \\\\| |// `.\n" +
                "                        / \\\\||| : |||// \\\n" +
                "                      / _||||| -:- |||||- \\\n" +
                "                        | | \\\\\\ - /// | |\n" +
                "                      | \\_| ''\\---/'' | |\n" +
                "                       \\ .-\\__ `-` ___/-. /\n" +
                "                    ___`. .' /--.--\\ `. . __\n" +
                "                 .\"\" '< `.___\\_<|>_/___.' >'\"\".\n" +
                "                | | : `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                "                  \\ \\ `-. \\_ __\\ /__ _/ .-` / /\n" +
                "          ======`-.____`-.___\\_____/___.-`____.-'======\n" +
                "                             `=---='\n" +
                "          .............................................\n" +
                "               佛曰：\n" +
                "                    爱老婆，爱生活\n");
    }
}
