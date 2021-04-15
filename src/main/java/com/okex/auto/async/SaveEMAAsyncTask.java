package com.okex.auto.async;

import com.okex.auto.common.EMACrossCal;
import com.okex.auto.common.OkexFutureRestAPI;
import com.okex.auto.common.OkexSwapRestAPI;
import com.okex.auto.constants.Constants;
import com.okex.auto.info.*;
import com.okex.auto.service.EMAHistoryService;
import com.okex.auto.service.OrderStatusService;
import com.okex.auto.service.OkexAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class SaveEMAAsyncTask {

    @Autowired
    EMAHistoryService emaHistoryService;

    @Autowired
    OkexFutureRestAPI okexFutureRestAPI;

    @Autowired
    OkexSwapRestAPI okexSwapRestAPI;

    @Autowired
    EMACrossCal emaCrossCal;

    @Autowired
    OkexAccountService okexAccountService;

    @Autowired
    FutureAsyncTask futureAsyncTask;

    @Autowired
    OrderStatusService orderStatusService;

    @Value("${candles.size.max}")
    Integer candlesMaxSize;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void saveFutureCurrentEMA(String currency, String alias) {

        String instrument_id = okexFutureRestAPI.OkexGetFutureInstrumentID(currency,alias);

        OkexEMAHistoryEntity findCondition = new OkexEMAHistoryEntity();
        findCondition.setCurrency(currency);
        findCondition.setAlias(alias);
        OkexEMAHistoryEntity lastEMAHistory = emaHistoryService.findCurrentEMAHistory(findCondition);


        SimpleDateFormat requestSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        requestSdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        SimpleDateFormat responseSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        responseSdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));

        try {

            Long start = lastEMAHistory.getTimestamp().getTime();
            String startTime = requestSdf.format(start);
            String endTime = requestSdf.format(new Date());
            Long end = requestSdf.parse(endTime).getTime();

            Long granularity = Long.parseLong(lastEMAHistory.getGranularity());

            String ema_low = lastEMAHistory.getEma_low();
            String ema_high = lastEMAHistory.getEma_high();

            String yesterdayCrossStatus = lastEMAHistory.getToday_cross();

            while(end - start >= granularity * 1000 * 2) {
                if(end - start >= granularity*1000*candlesMaxSize) {
                    endTime = requestSdf.format(start + granularity*1000*(candlesMaxSize-1));
                }

                List<OkexFutureCandle> okexFutureCandles = okexFutureRestAPI.OkexGetFutureCandle(instrument_id, startTime, endTime, granularity.toString());

                BigDecimal yesterdayEMALow = new BigDecimal(ema_low);
                BigDecimal yesterdayEMAHigh = new BigDecimal(ema_high);

                int beginIndex = 0;
                long notNewTime = new Date().getTime() - responseSdf.parse(okexFutureCandles.get(0).getTimestamp()).getTime();
                if(notNewTime <= granularity*1000) {
                    beginIndex = okexFutureCandles.size() - 2;
                } else {
                    beginIndex = okexFutureCandles.size() - 1;
                }

                for(int i=beginIndex;i>0;i--) {
                    //String timestamp = okexFutureCandles.get(i).getTimestamp().replaceAll("\\.[0-9][0-9][0-9]", "");
                    BigDecimal todayPrice = new BigDecimal(okexFutureCandles.get(i).getClose());
                    BigDecimal todayEMALow = emaCrossCal.emaCal(new BigDecimal(lastEMAHistory.getEma_rule_low()), yesterdayEMALow, todayPrice);
                    BigDecimal todayEMAHigh = emaCrossCal.emaCal(new BigDecimal(lastEMAHistory.getEma_rule_high()), yesterdayEMAHigh, todayPrice);
                    yesterdayEMALow = todayEMALow;
                    yesterdayEMAHigh = todayEMAHigh;
                    OkexEMAHistoryEntity todayEMAHistory = new OkexEMAHistoryEntity();
                    todayEMAHistory.setInstrument_id(instrument_id);
                    todayEMAHistory.setEma_low(yesterdayEMALow.toString());
                    todayEMAHistory.setEma_high(yesterdayEMAHigh.toString());
                    todayEMAHistory.setTimestamp(responseSdf.parse(okexFutureCandles.get(i).getTimestamp()));
                    todayEMAHistory.setYesterday_cross(yesterdayCrossStatus);
                    String todayCrossStatus = String.valueOf(todayEMALow.compareTo(todayEMAHigh));
                    todayEMAHistory.setToday_cross(todayCrossStatus);
                    todayEMAHistory.setUuid(lastEMAHistory.getUuid());
                    emaHistoryService.updateEMAHistory(todayEMAHistory);
                    yesterdayCrossStatus = todayCrossStatus;
                }

                lastEMAHistory = emaHistoryService.findCurrentEMAHistory(findCondition);
                start = lastEMAHistory.getTimestamp().getTime();
                startTime = requestSdf.format(start);
                endTime = requestSdf.format(new Date());;
                end = requestSdf.parse(endTime).getTime();
                ema_low = lastEMAHistory.getEma_low();
                ema_high = lastEMAHistory.getEma_high();

            }


            lastEMAHistory = emaHistoryService.findCurrentEMAHistory(findCondition);

            OkexAccount info = new OkexAccount();
            info.setAccount_status(Constants.API_STATUS_ENABLED);
            List<OkexAccount> okexAccountList = okexAccountService.findList(info);

            for(int i = 0; i< okexAccountList.size(); i++) {

                OkexOrderStatus orderStatusFindcondition = new OkexOrderStatus();
                orderStatusFindcondition.setCurrency(currency);
                orderStatusFindcondition.setAlias(alias);
                orderStatusFindcondition.setOkex_id(okexAccountList.get(i).getUuid());

                OkexOrderStatus orderStatus = orderStatusService.findOrderStatus(orderStatusFindcondition);

                if(orderStatus.getOrder_set_status().equals(Constants.API_STATUS_ENABLED)) {
                    if(orderStatus.getOrder_init_status().equals(Constants.API_STATUS_DISABLED)) {
                        if(futureAsyncTask.initAccount(currency, okexAccountList.get(i))) {
                            OkexOrderStatus newOrderStatus = new OkexOrderStatus();
                            newOrderStatus.setOkex_id(okexAccountList.get(i).getUuid());
                            newOrderStatus.setOrder_init_status(Constants.API_STATUS_ENABLED);
                            newOrderStatus.setAlias(Constants.ALIAS_QUARTER);
                            orderStatusService.updateOrderStatus(newOrderStatus);
                        } else {
                            continue;
                        }
                    }
                    futureAsyncTask.createOrder(currency, alias, instrument_id, lastEMAHistory.getToday_cross(), lastEMAHistory.getYesterday_cross(), okexAccountList.get(i));
                    futureAsyncTask.cancelAndCloseOrders(currency, alias, instrument_id, lastEMAHistory.getToday_cross(), lastEMAHistory.getYesterday_cross(), okexAccountList.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("saveEMAData: " + e.getMessage());
        }
    }
}
