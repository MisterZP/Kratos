package com.kratos.model;

import lombok.Data;

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
    private Status status;
    private String number;
    private List<String> months;
    private String time;
    private String cdnUrl;
    private boolean notsettled;
    private String reason;

    public Finance createFinance4This() {
        Finance clone = new Finance();
        clone.setContractName(new String(contractName));
        clone.setPlatName(new String(platName));
        clone.setStatusName(new String(statusName));
        clone.setStatus(this.status);
        clone.setNumber(new String(number));
        clone.setTime(new String(time));
        clone.setCdnUrl(new String(cdnUrl));
        clone.notsettled = this.notsettled;
        clone.months = Collections.unmodifiableList(this.months);
        return clone;
    }

    public enum Status {
        NOT_SETTLED(0),
        SETTLEMENT(1),
        REVIEW(1),
        NOT_PASS(1),
        DETERMINED(1),
        MAILED(1),
        SENT(1),
        WAITING_MONEY(1),
        CLOSED(2);
        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}

