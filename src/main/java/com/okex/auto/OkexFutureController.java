package com.okex.auto;

import com.okex.auto.async.AsyncExecutor;
import com.okex.auto.async.FutureAsyncTask;
import com.okex.auto.common.EMACrossCal;
import com.okex.auto.common.OkexFutureRestAPI;
import com.okex.auto.constants.Constants;
import com.okex.auto.info.*;
import com.okex.auto.service.EMAHistoryService;
import com.okex.auto.service.OrderRuleService;
import com.okex.auto.service.OrderStatusService;
import com.okex.auto.service.OkexAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableAutoConfiguration
@RestController
public class OkexFutureController {

    @Autowired
    OkexFutureRestAPI okexRestAPI;

    @Autowired
    EMACrossCal emaCrossCal;

    @Autowired
    OkexAccountService okexAccountService;

    @Autowired
    FutureAsyncTask futureAsyncTask;

    @Autowired
    EMAHistoryService emaHistoryService;

    @Autowired
    AsyncExecutor asyncExecutor;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    OrderRuleService orderRuleService;

    @RequestMapping("/api/general/v3/time")
    public OkexTimeStamp getServerTime() {
        return okexRestAPI.OkexGetGeneralTime();
    }

    @RequestMapping("/api/futures/v3/candles")
    public List<OkexFutureCandle> getFutureCandle() {
        String instrumentEos = okexRestAPI.OkexGetFutureInstrumentID("EOS",Constants.ALIAS_QUARTER);
        return okexRestAPI.OkexGetFutureCandle(instrumentEos, "2019-03-31T08:15:00Z", "2019-03-31T08:20:00Z", "300");
    }

    @RequestMapping("/api/futures/v3/instruments")
    public List<OkexFutureInstrument> getFutureInstruments() {
        return okexRestAPI.OkexGetFutureInstruments();
    }

    @RequestMapping("/api/futures/v3/eosInstrument")
    public String getFutureEOSInstrumentID() {
        return okexRestAPI.OkexGetFutureInstrumentID("EOS",Constants.ALIAS_QUARTER);
    }

    @RequestMapping("/api/futures/v3/instruments/eos/ticker")
    public OkexFutureTicker getEOSTicker() {
        String instrumentEos = okexRestAPI.OkexGetFutureInstrumentID("EOS",Constants.ALIAS_QUARTER);
        return okexRestAPI.OkexGetfutureticker(instrumentEos);
    }

    @RequestMapping("/crossTest")
    public String crossTest() {
        return null;
    }

    @RequestMapping("/api/futures/v3/position")
    public OkexFuturePosition getPosition(@RequestParam String apiID) {
        try {
            OkexAccount info = new OkexAccount();
            info.setUuid(apiID);
            info.setAccount_status(Constants.API_STATUS_ENABLED);
            OkexAccount OkexAccount = okexAccountService.findOne(info);
            String instrumentEos = okexRestAPI.OkexGetFutureInstrumentID("EOS",Constants.ALIAS_QUARTER);
            return okexRestAPI.OkexGetFuturePosition(instrumentEos, OkexAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/api/futures/v3/accounts/{currency}")
    public OkexFutureAccountCurrency getAccountCurrency(@PathVariable("currency") String currency, @RequestParam String apiID) {

        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexGetFutureAccountCurrency(currency, OkexAccount);
    }

    @RequestMapping("/api/futures/v3/orders/{instrument_id}")
    public OkexFutureOrderList getOrderList(@PathVariable("instrument_id") String instrument_id, @RequestParam String status, @RequestParam String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexGetFutureOrderList(instrument_id, status, OkexAccount);
    }

    @RequestMapping("/api/futures/v3/accounts/eos/leverage/{apiID}")
    public String setLeverage(@RequestBody OkexFutureLeverage okexFutureLeverage, @PathVariable String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexSetFutureLeverage(okexFutureLeverage, OkexAccount);
    }

    @RequestMapping(value = "api/futures/v3/order/{apiID}", method = RequestMethod.POST)
    public OkexFutureOrderInfo setOrder(@RequestBody OkexFutureOrderInfo orderInfo, @PathVariable String apiID) {

        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexSetFutureOrder(orderInfo, OkexAccount);
    }

    @RequestMapping("api/futures/v3/cancel_order/{apiID}")
    public OkexFutureOrderInfo canCelOrder(@RequestBody OkexFutureOrderInfo okexFutureOrderInfo, @PathVariable String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexCancelFutureOrder(okexFutureOrderInfo, OkexAccount);
    }

    @RequestMapping(value = "api/futures/v3/order", method = RequestMethod.GET)
    public OkexFutureOrderInfo getOrder(@RequestParam String instrumentId,@RequestParam String client_oid,@RequestParam String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexGetFutureOrderByClientOid(instrumentId, client_oid, OkexAccount);
    }

    @RequestMapping(value = "api/futures/v3/book/{instrumentId}")
    public OkexFutureBooks getBook(@PathVariable String instrumentId, @RequestParam String size) {
        return okexRestAPI.OkexGetFutureBooks(instrumentId, size);
    }

    @RequestMapping(value = "api/futures/v3/margin/{apiID}")
    public OkexFutureMarginMode setMarginMode(@RequestBody OkexFutureMarginMode okexFutureMarginMode, @PathVariable String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexSetFutureMarginMode(okexFutureMarginMode, OkexAccount);

    }

    @RequestMapping(value = "api/futures/v3/accounts/leverage/{currency}/{apiID}")
    public String getLeverage(@PathVariable String currency, @PathVariable String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount OkexAccount = okexAccountService.findOne(info);
        return okexRestAPI.OkexGetFutureLeverage(currency, OkexAccount);

    }

    @RequestMapping(value = "api/futures/v3/init/{apiID}")
    public String init(@PathVariable String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount okexAccount = okexAccountService.findOne(info);
        futureAsyncTask.initAccount("EOS", okexAccount);

        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
        okexOrderStatus.setOkex_id(apiID);
        okexOrderStatus.setCurrency("EOS");
        okexOrderStatus.setOrder_init_status(Constants.API_STATUS_ENABLED);

        okexOrderStatus.setAlias(Constants.ALIAS_QUARTER);
        orderStatusService.updateOrderStatus(okexOrderStatus);

        return null;
    }

    @RequestMapping(value = "api/futures/v3/setOrder/{apiID}")
    public String setOrder(@PathVariable String apiID) {
        OkexAccount info = new OkexAccount();
        info.setUuid(apiID);
        info.setAccount_status(Constants.API_STATUS_ENABLED);
        OkexAccount okexAccount = okexAccountService.findOne(info);

        String instrument_id = okexRestAPI.OkexGetFutureInstrumentID("EOS",Constants.ALIAS_QUARTER);

        OkexEMAHistoryEntity findCondition = new OkexEMAHistoryEntity();
        findCondition.setAlias(Constants.ALIAS_QUARTER);
        OkexEMAHistoryEntity lastEMAHistory = emaHistoryService.findCurrentEMAHistory(findCondition);

        OkexOrderStatus orderStatusFindcondition = new OkexOrderStatus();
        orderStatusFindcondition.setCurrency("EOS");
        orderStatusFindcondition.setAlias(Constants.ALIAS_QUARTER);
        orderStatusFindcondition.setOkex_id(apiID);

        OkexOrderStatus orderStatus = orderStatusService.findOrderStatus(orderStatusFindcondition);

        if(orderStatus.getOrder_set_status().equals(Constants.API_STATUS_ENABLED)) {
            if(orderStatus.getOrder_init_status().equals(Constants.API_STATUS_DISABLED)) {
                if(futureAsyncTask.initAccount("EOS", okexAccount)){
                    OkexOrderStatus newOrderStatus = new OkexOrderStatus();
                    newOrderStatus.setOkex_id(okexAccount.getUuid());
                    newOrderStatus.setOrder_init_status(Constants.API_STATUS_ENABLED);
                    newOrderStatus.setAlias(Constants.ALIAS_QUARTER);
                    orderStatusService.updateOrderStatus(newOrderStatus);
                } else {
                    return null;
                }
            }
            futureAsyncTask.createOrder("EOS", Constants.ALIAS_QUARTER, instrument_id, lastEMAHistory.getToday_cross(), lastEMAHistory.getYesterday_cross(), okexAccount);
            futureAsyncTask.cancelAndCloseOrders("EOS", Constants.ALIAS_QUARTER, instrument_id, lastEMAHistory.getToday_cross(), lastEMAHistory.getYesterday_cross(), okexAccount);
        }
        return null;
    }

    @RequestMapping(value = "api/futures/v3/saveEMA")
    public String saveEMA() {
        asyncExecutor.createOrderAndSaveEOSEMA();
        return null;
    }

    @RequestMapping(value = "api/futures/v3/hedgingOrder")
    public String hedgingOrder() {
        asyncExecutor.hedgingEOSOrder();
        return null;
    }

    @RequestMapping(value = "api/futures/v3/getOrderRule")
    public OkexOrderRule getOrderRule(@RequestParam String currency, @RequestParam String alias) {
        OkexOrderRule okexOrderRule = new OkexOrderRule();
        okexOrderRule.setCurrency(currency);
        okexOrderRule.setAlias(alias);
        return orderRuleService.findOrderRuleByCurrencyAndAlias(okexOrderRule);
    }
}
