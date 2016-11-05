package com.kratos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kratos.entity.CommonResponse;
import com.kratos.entity.Page;
import com.kratos.model.App;
import com.kratos.model.Finance;
import com.kratos.req.FinanceReq;
import com.kratos.wrapper.RestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zengping on 2016/11/2.
 */
@RestController
@RequestMapping("/rest")
public class RestAppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestAppController.class);
    @Autowired
    private RestWrapper restWrapper;

    @RequestMapping("/find/{appId}")
    public ResponseEntity<CommonResponse<App>> get4AppId(@PathVariable("appId") Long appId){
        return restWrapper.execute("http://api-service/api/find/" + appId,
                HttpMethod.POST, HttpEntity.EMPTY, new TypeReference<CommonResponse<App>>(){});
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
                HttpMethod.POST, new HttpEntity(req), new TypeReference<CommonResponse<Page<Finance>>>(){});
    }
}
