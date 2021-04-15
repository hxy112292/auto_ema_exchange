package com.okex.auto.async;

import com.okex.auto.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AsyncExecutor {

    @Autowired
    public SaveEMAAsyncTask saveEMAAsyncTask;

    @Autowired
    public HedgingAsyncTask hedgingAsyncTask;


    public Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Scheduled(cron = "5 0/15 * * * ?")
    public void createOrderAndSaveEOSEMA() {
        saveEMAAsyncTask.saveFutureCurrentEMA("EOS", Constants.ALIAS_QUARTER);
    }

    //@Scheduled(cron = "5 0/30 * * * ?")
//    public void createOrderAndSaveXRPEMA() {
//        saveEMAAsyncTask.saveFutureCurrentEMA("XRP", Constants.ALIAS_QUARTER);
//    }

    //@Scheduled(cron = "10-59/6 * * * * ?")
    public void hedgingEOSOrder() {
        hedgingAsyncTask.setHedgingOrder("EOS", Constants.ALIAS_QUARTER);
    }

    //@Scheduled(cron = "10-59/6 * * * * ?")
//    public void hedgingXRPOrder() {
//        hedgingAsyncTask.setHedgingOrder("XRP", Constants.ALIAS_QUARTER);
//    }
}
