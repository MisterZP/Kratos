package com.kratos.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zengping on 2016/11/4.
 */
@Data
public class Finance {
    private String contractName;
    private String platName;
    private String statusName;
    private String status;
    private String number;
    private List<String> months;
    private String time;
    private String cdnUrl;
    private boolean notsettled;

    public Finance createFinance4This() {
        Finance clone = new Finance();
        clone.setContractName(new String(contractName));
        clone.setPlatName(new String(platName));
        clone.setStatusName(new String(statusName));
        clone.setStatus(new String(status));
        clone.setNumber(new String(number));
        clone.setTime(new String(time));
        clone.setCdnUrl(new String(cdnUrl));
        clone.notsettled = this.notsettled;
        clone.months = Collections.unmodifiableList(new ArrayList<String>(months.size()));
        return clone;
    }

    public enum Status {
        NOT_SETTLED,
        SETTLEMENT,
        CLOSED
    }
}

