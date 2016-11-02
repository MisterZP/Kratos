package com.kratos.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kratos.entity.CommonResponse;
import com.kratos.model.App;
import com.kratos.wrapper.RestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zengping on 2016/11/2.
 */
@RestController
@RequestMapping("/rest")
public class RestAppController {
    @Autowired
    private RestWrapper restWrapper;

    @RequestMapping("/find/{appId}")
    public ResponseEntity<CommonResponse<App>> get4AppId(@PathVariable("appId") Long appId){
        return restWrapper.execute("http://api-service/api/find/" + appId,
                HttpMethod.POST, HttpEntity.EMPTY, new TypeReference<ResponseEntity<CommonResponse<App>>>(){});
    }
}
