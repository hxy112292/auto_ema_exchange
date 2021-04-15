package com.okex.auto.async;


import com.okex.auto.common.OkexFutureRestAPI;
import com.okex.auto.constants.Constants;
import com.okex.auto.info.OkexAccount;
import com.okex.auto.service.EMAHistoryService;
import com.okex.auto.service.OkexAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HedgingAsyncTask {

    @Autowired
    OkexFutureRestAPI okexFutureRestAPI;

    @Autowired
    OkexAccountService okexAccountService;

    @Autowired
    EMAHistoryService emaHistoryService;

    @Autowired
    FutureAsyncTask futureAsyncTask;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setHedgingOrder(String currency, String alias) {

        OkexAccount info = new OkexAccount();
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        List<OkexAccount> okexAccountList = okexAccountService.findList(info);

        for(int i = 0; i< okexAccountList.size(); i++) {
            futureAsyncTask.closeHalfOrder(currency, alias, okexAccountList.get(i));
        }


    }
}
