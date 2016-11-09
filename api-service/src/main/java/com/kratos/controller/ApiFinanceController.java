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
        LOGGER.info("request param: {}", new ObjectMapper().writeValueAsString(req));
        List<Finance> finances = new ArrayList<>();
        Finance finance = new Finance();
        finance.setCdnUrl("http://g3.letv.cn/228/1/8/tvstore_dev/0/1472118060_fe76ad974427ff4f6ba8ea228efcd32c.zip?tag=iptv&b=1800");
        finance.setTime("2016/03/24 12:23");
        finance.setNumber("147788647603402");
        finance.setStatus(Finance.Status.NOT_SETTLED);
        finance.setContractName("官网添加测试");
        finance.setNotsettled(true);
        finance.setStatusName("无结算申请");
        finance.setPlatName("手机");
        finance.setMonths(Arrays.asList(new String[]{"2016年09月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.NOT_SETTLED);
        finance.setContractName("列王的纷争");
        finance.setStatusName("待结算申请");
        finance.setNotsettled(false);
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.REVIEW);
        finance.setStatusName("结算审核中");
        finance.setContractName("功夫熊猫");
        finance.setMonths(Arrays.asList(new String[]{"2016年08月", "2016年09月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.NOT_PASS);
        finance.setStatusName("审核不通过");
        finance.setContractName("酷我音乐盒");
        finance.setReason("账单异常审核不通过");
        finance.setMonths(Arrays.asList(new String[]{"2016年08月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setReason(null);
        finance.setStatus(Finance.Status.DETERMINED);
        finance.setStatusName("账单待确定");
        finance.setContractName("刀塔传奇");
        finance.setMonths(Arrays.asList(new String[]{"2016年07月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.MAILED);
        finance.setStatusName("结算单待邮寄");
        finance.setContractName("盗墓笔记");
        finance.setMonths(Arrays.asList(new String[]{"2016年06月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.SENT);
        finance.setStatusName("结算单已寄出");
        finance.setContractName("勇者快跑");
        finance.setNotsettled(false);
        finance.setMonths(Arrays.asList(new String[]{"2016年05月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.WAITING_MONEY);
        finance.setStatusName("等待打款");
        finance.setContractName("众妖之怒");
        finance.setMonths(Arrays.asList(new String[]{"2016年12月"}));
        eqBean(req, finances, finance);
        finance = finance.createFinance4This();
        finance.setStatus(Finance.Status.CLOSED);
        finance.setContractName("魔兽世界");
        finance.setStatusName("已结算");
        finance.setMonths(Arrays.asList(new String[]{"2016年06月", "2016年011月"}));
        eqBean(req, finances, finance);
        Page<Finance> page = new Page(1, 10);
        page.setDatas(finances);
        return new ResponseEntity<>(CommonResponse.OK(page), HttpStatus.OK);
    }

    private void eqBean(FinanceReq req, List<Finance> finances, Finance finance) {
        if(StringUtils.isNotBlank(req.getContractName()) && finance.getContractName().contains(req.getContractName())
                && null != req.getStatus() && req.getStatus().getCode() == finance.getStatus().getCode()){
            finances.add(finance);
        }else if(StringUtils.isBlank(req.getContractName()) && req.getStatus().getCode() == finance.getStatus().getCode()){
            finances.add(finance);
        }
    }
}
