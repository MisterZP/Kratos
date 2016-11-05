package com.kratos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kratos.entity.CommonResponse;
import com.kratos.model.Finance;
import com.kratos.model.Page;
import com.kratos.req.FinanceReq;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zengping on 2016/11/4.
 */
@RestController
@RequestMapping("finance")
public class ApiFinanceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiFinanceController.class);

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<Page<Finance>>> getFinances(@RequestBody FinanceReq req) throws JsonProcessingException {
        LOGGER.info("request param: {}", new ObjectMapper().writer().writeValueAsString(req));
        List<Finance> finances = new ArrayList<>();
        Finance finance = new Finance();
        finance.setCdnUrl("http://g3.letv.cn/228/1/8/tvstore_dev/0/1472118060_fe76ad974427ff4f6ba8ea228efcd32c.zip?tag=iptv&b=1800");
        finance.setTime("2016/03/24");
        finance.setNumber("147788647603402");
        finance.setStatus(Finance.Status.NOT_SETTLED.name());
        finance.setContractName("刀塔传奇");
        finance.setNotsettled(true);
        finance.setStatusName("未结算");
        finance.setPlatName("手机");
        finance.setMonths(Arrays.asList(new String[]{"2016-09", "2016-10","2016-11"}));
        if(StringUtils.isNotBlank(req.getContractName()) && finance.equals(req.getContractName())){
            finances.add(finance);
        }else if (StringUtils.isBlank(req.getContractName())){
            finances.add(finance);
        }
        finance = finance.createFinance4This();
        finance.setContractName("列王的纷争");
        finance.setNotsettled(false);
        if(StringUtils.isNotBlank(req.getContractName()) && finance.equals(req.getContractName())){
            finances.add(finance);
        }else if (StringUtils.isBlank(req.getContractName())){
            finances.add(finance);
        }
        finance = finance.createFinance4This();
        finance.setContractName("功夫熊猫");
        finance.setNotsettled(false);
        finance.setMonths(Arrays.asList(new String[]{"2016-08", "2016-09"}));
        if(StringUtils.isNotBlank(req.getContractName()) && finance.equals(req.getContractName())){
            finances.add(finance);
        }else if (StringUtils.isBlank(req.getContractName())){
            finances.add(finance);
        }
        finances.add(finance);
        Page<Finance> page = new Page(1, 10);
        page.setDatas(finances);
        return new ResponseEntity<>(CommonResponse.OK(page), HttpStatus.OK);
    }
}
