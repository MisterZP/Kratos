package com.kratos.qpid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by zengping on 2016/11/16.
 */
@Component
@EnableJms
public class QpidReciver {
    private static final Logger LOGGER = LoggerFactory.getLogger(QpidReciver.class);
    @Autowired
    private TaskExecutor taskExecutor;

    @JmsListener(destination = "topicExchange")
    public void receiveMes(final String message) {
        LOGGER.info("Received message <" + message + ">");
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("success execute message deal");
            }
        });
    }
}
