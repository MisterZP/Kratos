package com.kratos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zengping on 2016/12/19.
 */
@RestController
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/redis/template_{redisV}", method = RequestMethod.POST)
    public ResponseEntity<Integer> get4AppId(@PathVariable("redisV") final Integer redisV) throws JsonProcessingException {
        LOGGER.info("redis test value: {}", redisV);
        redisTemplate.opsForSet().add("test_Integer", redisV+"");
        return new ResponseEntity<>(redisV, HttpStatus.OK);
    }

    @RequestMapping(value = "/redis/getJsessionId", method = RequestMethod.POST)
    public ResponseEntity<Object> getJsessionId(HttpServletRequest request) throws JsonProcessingException {
        return new ResponseEntity<>(request.getSession().getAttribute("testV"), HttpStatus.OK);
    }

    @RequestMapping(value = "/redis/putV2Session_{V}", method = RequestMethod.POST)
    public ResponseEntity<String> putV2Session(HttpServletRequest request, @PathVariable("V") String value) throws JsonProcessingException {
        request.getSession().setAttribute("testV", value);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
