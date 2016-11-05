package com.kratos.req;

import com.kratos.model.Finance;
import lombok.Data;

import java.util.List;

/**
 * Created by zengping on 2016/11/4.
 */
@Data
public class FinanceReq {
    private String contractName;
    private Finance.Status status;
    private List<String> appIds;
}
