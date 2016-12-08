package com.kratos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kratos.entity.CommonResponse;
import com.kratos.entity.Page;
import com.kratos.model.App;
import com.kratos.model.Finance;
import com.kratos.redis.shard.CacheClient;
import com.kratos.redis.shard.ShardRedisCallback;
import com.kratos.repository.AppElasticRepository;
import com.kratos.req.FinanceReq;
import com.kratos.wrapper.RestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by zengping on 2016/11/2.
 */
@RestController
@RequestMapping("/rest")
public class RestAppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestAppController.class);
    @Autowired
    private RestWrapper restWrapper;
    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private AppElasticRepository elasticRepository;

    @RequestMapping("/find/{appId}")
    public ResponseEntity<CommonResponse<App>> get4AppId(@PathVariable("appId") Long appId) {
        return restWrapper.execute("http://api-service/api/find/" + appId,
                HttpMethod.POST, HttpEntity.EMPTY, new TypeReference<CommonResponse<App>>() {
                });
    }

    @RequestMapping(value = "/finance/list", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<Page<Finance>>> get4AppId(@RequestBody FinanceReq req) throws JsonProcessingException {
        LOGGER.info("request http://api-service/finance/list 接口");
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        req.setContractName("功夫熊猫");
        HttpEntity<String> formEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(req), headers);*/
        return restWrapper.execute("http://api-service/finance/list",
                HttpMethod.POST, new HttpEntity(req), new TypeReference<CommonResponse<Page<Finance>>>() {
                });
    }

    @RequestMapping(value = "/redis/test_{redisV}", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<Integer>> get4AppId(@PathVariable("redisV") final Integer redisV) throws JsonProcessingException {
        LOGGER.info("redis test value: {}", redisV);
        cacheClient.execute(new ShardRedisCallback<Integer>() {
            @Override
            public Integer doWithRedis(ShardedJedis shardedJedis) {
                shardedJedis.set("spring_cloud_test", redisV.toString());
                return redisV;
            }
        });
        Integer testV = cacheClient.execute(new ShardRedisCallback<Integer>() {
            @Override
            public Integer doWithRedis(ShardedJedis shardedJedis) {
                String v = shardedJedis.get("spring_cloud_test");
                return Integer.parseInt(v);
            }
        });
        return new ResponseEntity<>(CommonResponse.OK(HttpStatus.OK.value(), testV), HttpStatus.OK);
    }

    @RequestMapping(value = "/amqp/test_{message}", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse> get4AppId(@PathVariable("message") final String redisV) throws JsonProcessingException {
        jmsTemplate.convertAndSend(redisV);
        return new ResponseEntity<CommonResponse>(CommonResponse.OK(redisV), HttpStatus.OK);
    }

    @RequestMapping("/save_es/{appId}")
    public ResponseEntity<CommonResponse<App>> saveApp2Es(@PathVariable("appId") Long appId) {
        ResponseEntity<CommonResponse<App>> response = restWrapper.execute("http://api-service/api/find/" + appId,
                HttpMethod.POST, HttpEntity.EMPTY, new TypeReference<CommonResponse<App>>() {
                });
        if (null != response && null != response.getBody()) {
            App app = response.getBody().getEntity();
            elasticRepository.save(app);
        }
        return response;
    }

    @RequestMapping("/search_es/{appId}")
    public ResponseEntity<CommonResponse<App>> findApp4Es(@PathVariable("appId") Long appId) {
        return new ResponseEntity(CommonResponse.OK(HttpStatus.OK.value(), elasticRepository.findByAppIdOrAppKey(appId, null)), HttpStatus.OK);
    }
}
