package com.kratos.controller;

import com.kratos.entity.CommonResponse;
import com.kratos.model.App;
import com.kratos.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zengping on 2016/11/2.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AppService appService;

    @RequestMapping("/find/{appId}")
    public ResponseEntity<CommonResponse<App>> findApp4Id(@PathVariable("appId") Long id){
        return new ResponseEntity<>(CommonResponse.OK(HttpStatus.OK.value(), appService.getById(id)), HttpStatus.OK);
    }
}
