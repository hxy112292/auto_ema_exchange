package com.okex.auto.async;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.okex.auto.common.OkexFutureRestAPI;
import com.okex.auto.constants.Constants;
import com.okex.auto.info.*;
import com.okex.auto.service.EMAHistoryService;
import com.okex.auto.service.OrderStatusService;
import com.okex.auto.service.OrderRuleService;
import com.okex.auto.service.OkexAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FutureAsyncTask {

    @Autowired
    OkexFutureRestAPI okexFutureRestAPI;

    @Autowired
    EMAHistoryService emaHistoryService;

    @Autowired
    OkexAccountService okexAccountService;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    OrderRuleService orderRuleService;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void createOrder(String currency1, String alias, String instrument_id, String todayCrossStatus, String yesterDayCrossStatus, OkexAccount okexAccount) {

        OkexOrderRule orderRuleFindCondition = new OkexOrderRule();
        orderRuleFindCondition.setAlias(alias);
        orderRuleFindCondition.setCurrency(currency1);
        OkexOrderRule okexOrderRule = orderRuleService.findOrderRuleByCurrencyAndAlias(orderRuleFindCondition);

        String price = okexFutureRestAPI.OkexGetfutureticker(instrument_id).getLast();
        String balance = null;
        try{
            balance = okexFutureRestAPI.OkexGetFutureAccountCurrency(currency1, okexAccount).getEquity();
        } catch (Exception e) {
            logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,OkexGetFutureAccountCurrency," + e.getMessage());
            e.printStackTrace();
            return;
        }

        BigDecimal tmpSize = new BigDecimal(price).multiply(new BigDecimal(balance)).multiply(new BigDecimal(2))
                .multiply(new BigDecimal(okexOrderRule.getPosition_size())).setScale(0, BigDecimal.ROUND_DOWN);

        if (!todayCrossStatus.equals(yesterDayCrossStatus)) {
            if (todayCrossStatus.equals(Constants.CROSS_GOLD)) {

                OkexFutureBooks okexFutureBooks = okexFutureRestAPI.OkexGetFutureBooks(instrument_id, okexOrderRule.getDeep_size().toString());
                Long restingOrder = 0L;
                for(int i=0;i<okexOrderRule.getDeep_size();i++) {
                    restingOrder += Long.parseLong(okexFutureBooks.getBids().get(i).get(1)+"");
                }
                BigDecimal restingSize = new BigDecimal(restingOrder).divide(new BigDecimal(20), 0, BigDecimal.ROUND_DOWN);
                String orderCreateSize;
                if(restingSize.compareTo(tmpSize) >= 0) {
                    orderCreateSize = tmpSize.toString();
                } else {
                    orderCreateSize = restingSize.toString();
                }

                try {
                    if(Long.parseLong(orderCreateSize)>0L) {
                        OkexFutureOrderInfo createLongOrder = new OkexFutureOrderInfo();
                        createLongOrder.setInstrument_id(instrument_id);
                        createLongOrder.setType(Constants.CREATE_LONG);
                        createLongOrder.setOrder_type(Constants.ORDER_COMMON);
                        createLongOrder.setSize(orderCreateSize);
                        createLongOrder.setMatch_price(Constants.MATCH_PRICE);
                        createLongOrder.setLeverage(okexOrderRule.getLeverage());
                        createLongOrder.setPrice(null);
                        okexFutureRestAPI.OkexSetFutureOrder(createLongOrder, okexAccount);

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency1);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_hedging_status(Constants.API_STATUS_DISABLED);
                        okexOrderStatus.setOrder_stop_loss_status(Constants.API_STATUS_DISABLED);
                        okexOrderStatus.setOrder_stop_profit_status(Constants.API_STATUS_DISABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":create a longOrder");
                    }
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,create a longOrder," + e.getMessage());
                    e.printStackTrace();
                }
            } else if (todayCrossStatus.equals(Constants.CROSS_DEAD)) {

                OkexFutureBooks okexFutureBooks = okexFutureRestAPI.OkexGetFutureBooks(instrument_id, okexOrderRule.getDeep_size().toString());
                Long restingOrder = 0L;
                for(int i=0;i<okexOrderRule.getDeep_size();i++) {
                    restingOrder += Long.parseLong(okexFutureBooks.getAsks().get(i).get(1)+"");
                }
                BigDecimal restingSize = new BigDecimal(restingOrder).divide(new BigDecimal(20), 0, BigDecimal.ROUND_DOWN);
                String orderCreateSize;
                if(restingSize.compareTo(tmpSize) >= 0) {
                    orderCreateSize = tmpSize.toString();
                } else {
                    orderCreateSize = restingSize.toString();
                }
                try {
                    if(Long.parseLong(orderCreateSize)>0L) {
                        OkexFutureOrderInfo createShortOrder = new OkexFutureOrderInfo();
                        createShortOrder.setInstrument_id(instrument_id);
                        createShortOrder.setType(Constants.CREATE_SHORT);
                        createShortOrder.setOrder_type(Constants.ORDER_COMMON);
                        createShortOrder.setSize(orderCreateSize);
                        createShortOrder.setMatch_price(Constants.MATCH_PRICE);
                        createShortOrder.setLeverage(okexOrderRule.getLeverage());
                        createShortOrder.setPrice(null);
                        okexFutureRestAPI.OkexSetFutureOrder(createShortOrder, okexAccount);

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency1);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_hedging_status(Constants.API_STATUS_DISABLED);
                        okexOrderStatus.setOrder_stop_loss_status(Constants.API_STATUS_DISABLED);
                        okexOrderStatus.setOrder_stop_profit_status(Constants.API_STATUS_DISABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":create a shortOrder");
                    }
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,create a shortOrder," + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @Async
    public void cancelAndCloseOrders(String currency1, String alias, String instrument_id, String todayCrossStatus, String yesterDayCrossStatus, OkexAccount okexAccount) {


        OkexOrderRule orderRuleFindCondition = new OkexOrderRule();
        orderRuleFindCondition.setAlias(alias);
        orderRuleFindCondition.setCurrency(currency1);
        OkexOrderRule okexOrderRule = orderRuleService.findOrderRuleByCurrencyAndAlias(orderRuleFindCondition);

        if (!todayCrossStatus.equals(yesterDayCrossStatus)) {
            if (todayCrossStatus.equals(Constants.CROSS_GOLD)) {
                try {
                    OkexFutureOrderList waitOrderList = okexFutureRestAPI.OkexGetFutureOrderList(instrument_id, Constants.ORDER_STATUS_WAIT, okexAccount);
                    for(int i=0;i<waitOrderList.getOrder_info().size();i++) {
                        if(waitOrderList.getOrder_info().get(i).getType().equals(Constants.CREATE_SHORT)) {
                            OkexFutureOrderInfo waitOrder = new OkexFutureOrderInfo();
                            waitOrder.setInstrument_id(waitOrderList.getOrder_info().get(i).getInstrument_id());
                            waitOrder.setOrder_id(waitOrderList.getOrder_info().get(i).getOrder_id());
                            okexFutureRestAPI.OkexCancelFutureOrder(waitOrder, okexAccount);
                        }
                    }
                    logger.info(instrument_id + " " + okexAccount.getWxid() + ":cancel waitShortOrders");
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,cancel waitShortOrders," + e.getMessage());
                    e.printStackTrace();
                }

                try {
                    OkexFutureOrderList partOrderList = okexFutureRestAPI.OkexGetFutureOrderList(instrument_id, Constants.ORDER_STATUS_PART, okexAccount);
                    for(int i=0;i<partOrderList.getOrder_info().size();i++) {
                        if(partOrderList.getOrder_info().get(i).getType().equals(Constants.CREATE_SHORT)) {
                            OkexFutureOrderInfo partOrder = new OkexFutureOrderInfo();
                            partOrder.setInstrument_id(partOrderList.getOrder_info().get(i).getInstrument_id());
                            partOrder.setOrder_id(partOrderList.getOrder_info().get(i).getOrder_id());
                            okexFutureRestAPI.OkexCancelFutureOrder(partOrder, okexAccount);
                        }
                    }
                    logger.info(instrument_id + " " + okexAccount.getWxid() + ":cancel partShortOrders");
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,cancel partShortOrders," + e.getMessage());
                    e.printStackTrace();
                }
                try {
                    OkexFuturePosition okexFuturePosition = okexFutureRestAPI.OkexGetFuturePosition(instrument_id, okexAccount);
                    for(int i=0; i<okexFuturePosition.getHolding().size();i++) {
                        if(!okexFuturePosition.getHolding().get(i).getShort_avail_qty().equals("0")) {
                            OkexFutureOrderInfo closeShortOrder = new OkexFutureOrderInfo();
                            closeShortOrder.setInstrument_id(okexFuturePosition.getHolding().get(i).getInstrument_id());
                            closeShortOrder.setType(Constants.CLOSE_SHORT);
                            closeShortOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeShortOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeShortOrder.setLeverage(okexOrderRule.getLeverage());
                            closeShortOrder.setPrice(null);
                            closeShortOrder.setSize(okexFuturePosition.getHolding().get(i).getShort_avail_qty());
                            okexFutureRestAPI.OkexSetFutureOrder(closeShortOrder, okexAccount);
                        }
                    }
                    logger.info(instrument_id + " " + okexAccount.getWxid() + ":close shortOrders");
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,close shortOrders," + e.getMessage());
                    e.printStackTrace();
                }
            } else if (todayCrossStatus.equals(Constants.CROSS_DEAD)) {
                try {
                    OkexFutureOrderList waitOrderList = okexFutureRestAPI.OkexGetFutureOrderList(instrument_id, Constants.ORDER_STATUS_WAIT, okexAccount);
                    if(waitOrderList.getOrder_info().size() != 0) {
                        for(int i=0;i<waitOrderList.getOrder_info().size();i++) {
                            if(waitOrderList.getOrder_info().get(i).getType().equals(Constants.CREATE_LONG)) {
                                OkexFutureOrderInfo waitOrder = new OkexFutureOrderInfo();
                                waitOrder.setInstrument_id(waitOrderList.getOrder_info().get(i).getInstrument_id());
                                waitOrder.setOrder_id(waitOrderList.getOrder_info().get(i).getOrder_id());
                                okexFutureRestAPI.OkexCancelFutureOrder(waitOrder, okexAccount);
                            }
                        }
                    }
                    logger.info(instrument_id + " " + okexAccount.getWxid() + ":cancel waitLongOrders");
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,cancel waitLongOrders," + e.getMessage());
                    e.printStackTrace();
                }

                try {
                    OkexFutureOrderList partOrderList = okexFutureRestAPI.OkexGetFutureOrderList(instrument_id, Constants.ORDER_STATUS_PART, okexAccount);
                    if(partOrderList.getOrder_info().size() != 0) {
                        for(int i=0;i<partOrderList.getOrder_info().size();i++) {
                            if(partOrderList.getOrder_info().get(i).getType().equals(Constants.CREATE_LONG)) {
                                OkexFutureOrderInfo partOrder = new OkexFutureOrderInfo();
                                partOrder.setInstrument_id(partOrderList.getOrder_info().get(i).getInstrument_id());
                                partOrder.setOrder_id(partOrderList.getOrder_info().get(i).getOrder_id());
                                okexFutureRestAPI.OkexCancelFutureOrder(partOrder, okexAccount);
                            }
                        }
                    }
                    logger.info(instrument_id + " " + okexAccount.getWxid() + ":cancel partLongOrders");
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,cancel partLongOrders," + e.getMessage());
                    e.printStackTrace();
                }
                try {
                    OkexFuturePosition okexFuturePosition = okexFutureRestAPI.OkexGetFuturePosition(instrument_id, okexAccount);
                    for(int i=0; i<okexFuturePosition.getHolding().size();i++) {
                        if(!okexFuturePosition.getHolding().get(i).getLong_avail_qty().equals("0")) {
                            OkexFutureOrderInfo closeLongOrder = new OkexFutureOrderInfo();
                            closeLongOrder.setInstrument_id(okexFuturePosition.getHolding().get(i).getInstrument_id());
                            closeLongOrder.setType(Constants.CLOSE_LONG);
                            closeLongOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeLongOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeLongOrder.setLeverage(okexOrderRule.getLeverage());
                            closeLongOrder.setPrice(null);
                            closeLongOrder.setSize(okexFuturePosition.getHolding().get(i).getLong_avail_qty());
                            okexFutureRestAPI.OkexSetFutureOrder(closeLongOrder, okexAccount);
                        }
                    }
                    logger.info(instrument_id + " " + okexAccount.getWxid() + ":close longOrders");
                } catch (Exception e) {
                    logger.error(instrument_id + " " + okexAccount.getWxid() + ":setOrder,close longOrders," + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @Async
    public void closeHalfOrder(String currency, String alias, OkexAccount okexAccount) {

        OkexOrderStatus orderStatusFindCondition = new OkexOrderStatus();
        orderStatusFindCondition.setCurrency(currency);
        orderStatusFindCondition.setAlias(alias);
        orderStatusFindCondition.setOkex_id(okexAccount.getUuid());
        OkexOrderStatus findOrderStatusResult = orderStatusService.findOrderStatus(orderStatusFindCondition);

        OkexOrderRule orderRuleFindCondition = new OkexOrderRule();
        orderRuleFindCondition.setAlias(alias);
        orderRuleFindCondition.setCurrency(currency);
        OkexOrderRule okexOrderRule = orderRuleService.findOrderRuleByCurrencyAndAlias(orderRuleFindCondition);
        try {
            String instrument_id = okexFutureRestAPI.OkexGetFutureInstrumentID(currency, alias);
            OkexFuturePosition okexFuturePosition = okexFutureRestAPI.OkexGetFuturePosition(instrument_id, okexAccount);
            for(int i=0; i<okexFuturePosition.getHolding().size();i++) {

                OkexFuturePositionHolding okexFuturePositionHolding = okexFuturePosition.getHolding().get(i);

                if(findOrderStatusResult.getOrder_stop_loss_status().equals(Constants.API_STATUS_DISABLED)) {
                    Float shortPnlRatio = Float.parseFloat(okexFuturePositionHolding.getShort_pnl_ratio());
                    Float longPnlRatio = Float.parseFloat(okexFuturePositionHolding.getLong_pnl_ratio());
                    if ((!okexFuturePositionHolding.getShort_avail_qty().equals("0")) && shortPnlRatio < okexOrderRule.getStop_loss_ratio()) {

                        BigDecimal orderSize = new BigDecimal(okexFuturePositionHolding.getShort_avail_qty());
                        BigDecimal halfSize = orderSize.multiply(new BigDecimal(okexOrderRule.getStop_loss_size())).setScale(0, BigDecimal.ROUND_DOWN);

                        if (halfSize.compareTo(new BigDecimal(1)) == 1) {
                            OkexFutureOrderInfo closeShortOrder = new OkexFutureOrderInfo();
                            closeShortOrder.setInstrument_id(okexFuturePositionHolding.getInstrument_id());
                            closeShortOrder.setType(Constants.CLOSE_SHORT);
                            closeShortOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeShortOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeShortOrder.setLeverage(okexOrderRule.getLeverage());
                            closeShortOrder.setPrice(null);
                            closeShortOrder.setSize(halfSize.toString());
                            okexFutureRestAPI.OkexSetFutureOrder(closeShortOrder, okexAccount);
                        }

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_hedging_status(Constants.API_STATUS_ENABLED);
                        okexOrderStatus.setOrder_stop_loss_status(Constants.API_STATUS_ENABLED);
                        okexOrderStatus.setOrder_stop_profit_status(Constants.API_STATUS_ENABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":stop loss AllShortOrders");
                    }
                    if ((!okexFuturePositionHolding.getLong_avail_qty().equals("0")) && longPnlRatio < okexOrderRule.getStop_loss_ratio()) {

                        BigDecimal orderSize = new BigDecimal(okexFuturePositionHolding.getLong_avail_qty());
                        BigDecimal halfSize = orderSize.multiply(new BigDecimal(okexOrderRule.getStop_loss_size())).setScale(0, BigDecimal.ROUND_DOWN);

                        if (halfSize.compareTo(new BigDecimal(1)) == 1) {
                            OkexFutureOrderInfo closeLongOrder = new OkexFutureOrderInfo();
                            closeLongOrder.setInstrument_id(okexFuturePositionHolding.getInstrument_id());
                            closeLongOrder.setType(Constants.CLOSE_LONG);
                            closeLongOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeLongOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeLongOrder.setLeverage(okexOrderRule.getLeverage());
                            closeLongOrder.setPrice(null);
                            closeLongOrder.setSize(okexFuturePositionHolding.getLong_avail_qty());
                            okexFutureRestAPI.OkexSetFutureOrder(closeLongOrder, okexAccount);
                        }

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_hedging_status(Constants.API_STATUS_ENABLED);
                        okexOrderStatus.setOrder_stop_loss_status(Constants.API_STATUS_ENABLED);
                        okexOrderStatus.setOrder_stop_profit_status(Constants.API_STATUS_ENABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":stop loss AllLongOrders");
                    }
                }

                findOrderStatusResult = orderStatusService.findOrderStatus(orderStatusFindCondition);

                if(findOrderStatusResult.getOrder_stop_profit_status().equals(Constants.API_STATUS_DISABLED)) {
                    Float shortPnlRatio = Float.parseFloat(okexFuturePositionHolding.getShort_pnl_ratio());
                    Float longPnlRatio = Float.parseFloat(okexFuturePositionHolding.getLong_pnl_ratio());
                    if ((!okexFuturePositionHolding.getShort_avail_qty().equals("0")) && shortPnlRatio >= okexOrderRule.getStop_profit_ratio()) {

                        BigDecimal orderSize = new BigDecimal(okexFuturePositionHolding.getShort_avail_qty());
                        BigDecimal halfSize = orderSize.multiply(new BigDecimal(okexOrderRule.getStop_profit_size())).setScale(0, BigDecimal.ROUND_DOWN);

                        if (halfSize.compareTo(new BigDecimal(1)) == 1) {
                            OkexFutureOrderInfo closeShortOrder = new OkexFutureOrderInfo();
                            closeShortOrder.setInstrument_id(okexFuturePositionHolding.getInstrument_id());
                            closeShortOrder.setType(Constants.CLOSE_SHORT);
                            closeShortOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeShortOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeShortOrder.setLeverage(okexOrderRule.getLeverage());
                            closeShortOrder.setPrice(null);
                            closeShortOrder.setSize(halfSize.toString());
                            okexFutureRestAPI.OkexSetFutureOrder(closeShortOrder, okexAccount);
                        }

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_stop_profit_status(Constants.API_STATUS_ENABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":stop profit halfShortOrders");
                    }
                    if ((!okexFuturePositionHolding.getLong_avail_qty().equals("0")) && longPnlRatio >= okexOrderRule.getStop_profit_ratio()) {

                        BigDecimal orderSize = new BigDecimal(okexFuturePositionHolding.getLong_avail_qty());
                        BigDecimal halfSize = orderSize.multiply(new BigDecimal(okexOrderRule.getStop_profit_size())).setScale(0, BigDecimal.ROUND_DOWN);

                        if (halfSize.compareTo(new BigDecimal(1)) == 1) {
                            OkexFutureOrderInfo closeLongOrder = new OkexFutureOrderInfo();
                            closeLongOrder.setInstrument_id(okexFuturePositionHolding.getInstrument_id());
                            closeLongOrder.setType(Constants.CLOSE_LONG);
                            closeLongOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeLongOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeLongOrder.setLeverage(okexOrderRule.getLeverage());
                            closeLongOrder.setPrice(null);
                            closeLongOrder.setSize(halfSize.toString());
                            okexFutureRestAPI.OkexSetFutureOrder(closeLongOrder, okexAccount);
                        }

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_stop_profit_status(Constants.API_STATUS_ENABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":stop profit halfLongOrders");
                    }
                }

                if(findOrderStatusResult.getOrder_hedging_status().equals(Constants.API_STATUS_DISABLED)) {
                    Float shortPnlRatio = Float.parseFloat(okexFuturePositionHolding.getShort_pnl_ratio());
                    Float longPnlRatio = Float.parseFloat(okexFuturePositionHolding.getLong_pnl_ratio());
                    if ((!okexFuturePositionHolding.getShort_avail_qty().equals("0")) && shortPnlRatio >= okexOrderRule.getHedging_ratio() && shortPnlRatio < okexOrderRule.getStop_profit_ratio()) {

                        BigDecimal orderSize = new BigDecimal(okexFuturePositionHolding.getShort_avail_qty());
                        BigDecimal halfSize = orderSize.multiply(new BigDecimal(okexOrderRule.getHedging_size())).setScale(0, BigDecimal.ROUND_DOWN);

                        if (halfSize.compareTo(new BigDecimal(1)) == 1) {
                            OkexFutureOrderInfo closeShortOrder = new OkexFutureOrderInfo();
                            closeShortOrder.setInstrument_id(okexFuturePositionHolding.getInstrument_id());
                            closeShortOrder.setType(Constants.CLOSE_SHORT);
                            closeShortOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeShortOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeShortOrder.setLeverage(okexOrderRule.getLeverage());
                            closeShortOrder.setPrice(null);
                            closeShortOrder.setSize(halfSize.toString());
                            okexFutureRestAPI.OkexSetFutureOrder(closeShortOrder, okexAccount);
                        }

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_hedging_status(Constants.API_STATUS_ENABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":hedge halfShortOrders");
                    }
                    if ((!okexFuturePositionHolding.getLong_avail_qty().equals("0")) && longPnlRatio >= okexOrderRule.getHedging_ratio() && longPnlRatio < okexOrderRule.getStop_profit_ratio()) {

                        BigDecimal orderSize = new BigDecimal(okexFuturePositionHolding.getLong_avail_qty());
                        BigDecimal halfSize = orderSize.multiply(new BigDecimal(okexOrderRule.getHedging_size())).setScale(0, BigDecimal.ROUND_DOWN);

                        if (halfSize.compareTo(new BigDecimal(1)) == 1) {
                            OkexFutureOrderInfo closeLongOrder = new OkexFutureOrderInfo();
                            closeLongOrder.setInstrument_id(okexFuturePositionHolding.getInstrument_id());
                            closeLongOrder.setType(Constants.CLOSE_LONG);
                            closeLongOrder.setOrder_type(Constants.ORDER_COMMON);
                            closeLongOrder.setMatch_price(Constants.MATCH_PRICE);
                            closeLongOrder.setLeverage(okexOrderRule.getLeverage());
                            closeLongOrder.setPrice(null);
                            closeLongOrder.setSize(halfSize.toString());
                            okexFutureRestAPI.OkexSetFutureOrder(closeLongOrder, okexAccount);
                        }

                        OkexOrderStatus okexOrderStatus = new OkexOrderStatus();
                        okexOrderStatus.setCurrency(currency);
                        okexOrderStatus.setAlias(alias);
                        okexOrderStatus.setOkex_id(okexAccount.getUuid());
                        okexOrderStatus.setOrder_hedging_status(Constants.API_STATUS_ENABLED);
                        orderStatusService.updateOrderStatus(okexOrderStatus);
                        logger.info(instrument_id + " " + okexAccount.getWxid() + ":hedge halfLongOrders");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(currency + alias + " " + okexAccount.getWxid() + ":setOrder,close halfOrders," + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean initAccount(String currency, OkexAccount okexAccount) {

        try {
            String instrument_id_quarter = okexFutureRestAPI.OkexGetFutureInstrumentID(currency, Constants.ALIAS_QUARTER);
            if(!closeAllOrders(instrument_id_quarter, okexAccount)) {
                return false;
            }

            String instrument_id_next_week = okexFutureRestAPI.OkexGetFutureInstrumentID(currency, Constants.ALIAS_NEXT_WEEK);
            if(!closeAllOrders(instrument_id_next_week, okexAccount)) {
                return false;
            }

            String instrument_id_this_week = okexFutureRestAPI.OkexGetFutureInstrumentID(currency, Constants.ALIAS_THIS_WEEK);
            if(!closeAllOrders(instrument_id_this_week, okexAccount)) {
                return false;
            }

            if(!setMargin(currency, okexAccount)) {
                return false;
            }

            if(!setLeverage(currency, Constants.ALIAS_QUARTER, instrument_id_quarter, okexAccount)) {
                return false;
            }
        } catch (Exception e) {
            logger.error(okexAccount.getWxid() + ":" + currency + ", initAccount" + e.getMessage());
            e.printStackTrace();
            return false;
        }

        logger.info(okexAccount.getWxid() + ":" + currency + ", initAccount finished.");

        return true;
    }

    public boolean closeAllOrders(String instrument_id, OkexAccount okexAccount) {
        try {
            OkexFutureOrderList waitOrderList = okexFutureRestAPI.OkexGetFutureOrderList(instrument_id, Constants.ORDER_STATUS_WAIT, okexAccount);
            for (int i = 0; i < waitOrderList.getOrder_info().size(); i++) {
                OkexFutureOrderInfo waitOrder = new OkexFutureOrderInfo();
                waitOrder.setInstrument_id(waitOrderList.getOrder_info().get(i).getInstrument_id());
                waitOrder.setOrder_id(waitOrderList.getOrder_info().get(i).getOrder_id());
                okexFutureRestAPI.OkexCancelFutureOrder(waitOrder, okexAccount);
            }
        } catch (Exception e) {
            logger.error(instrument_id + " " + okexAccount.getWxid() + ":initAccount,cancel waitOrders," + e.getMessage());
            e.printStackTrace();
            return false;
        }

        try {
            OkexFutureOrderList partOrderList = okexFutureRestAPI.OkexGetFutureOrderList(instrument_id, Constants.ORDER_STATUS_PART, okexAccount);
            for (int i = 0; i < partOrderList.getOrder_info().size(); i++) {
                OkexFutureOrderInfo partOrder = new OkexFutureOrderInfo();
                partOrder.setInstrument_id(partOrderList.getOrder_info().get(i).getInstrument_id());
                partOrder.setOrder_id(partOrderList.getOrder_info().get(i).getOrder_id());
                okexFutureRestAPI.OkexCancelFutureOrder(partOrder, okexAccount);
            }
        } catch (Exception e) {
            logger.error(instrument_id + " " + okexAccount.getWxid() + ":initAccount,cancel partOrders," + e.getMessage());
            e.printStackTrace();
            return false;
        }

        try {
            String currency = instrument_id.substring(0, 3);
            String jsonstr = okexFutureRestAPI.OkexGetFutureLeverage(currency, okexAccount);
            JsonObject jsonObject = new JsonParser().parse(jsonstr).getAsJsonObject();

            String longLeverage;
            String shortLeverage;

            if (jsonObject.get("margin_mode").getAsString().equals(Constants.MARGIN_MODE_CROSSED)) {
                longLeverage = jsonObject.get("leverage").getAsString();
                shortLeverage = jsonObject.get("leverage").getAsString();
            } else {
                longLeverage = jsonObject.get(instrument_id).getAsJsonObject().get("long_leverage").getAsString();
                shortLeverage = jsonObject.get(instrument_id).getAsJsonObject().get("short_leverage").getAsString();
            }


            OkexFuturePosition okexFuturePosition = okexFutureRestAPI.OkexGetFuturePosition(instrument_id, okexAccount);
            for (int i = 0; i < okexFuturePosition.getHolding().size(); i++) {
                if (!okexFuturePosition.getHolding().get(i).getLong_avail_qty().equals("0")) {
                    OkexFutureOrderInfo closeLongOrder = new OkexFutureOrderInfo();
                    closeLongOrder.setInstrument_id(okexFuturePosition.getHolding().get(i).getInstrument_id());
                    closeLongOrder.setType(Constants.CLOSE_LONG);
                    closeLongOrder.setOrder_type(Constants.ORDER_COMMON);
                    closeLongOrder.setMatch_price(Constants.MATCH_PRICE);
                    closeLongOrder.setLeverage(longLeverage);
                    closeLongOrder.setPrice(null);
                    closeLongOrder.setSize(okexFuturePosition.getHolding().get(i).getLong_avail_qty());
                    okexFutureRestAPI.OkexSetFutureOrder(closeLongOrder, okexAccount);
                }
                if (!okexFuturePosition.getHolding().get(i).getShort_avail_qty().equals("0")) {
                    OkexFutureOrderInfo closeShortOrder = new OkexFutureOrderInfo();
                    closeShortOrder.setInstrument_id(okexFuturePosition.getHolding().get(i).getInstrument_id());
                    closeShortOrder.setType(Constants.CLOSE_SHORT);
                    closeShortOrder.setOrder_type(Constants.ORDER_COMMON);
                    closeShortOrder.setMatch_price(Constants.MATCH_PRICE);
                    closeShortOrder.setLeverage(shortLeverage);
                    closeShortOrder.setPrice(null);
                    closeShortOrder.setSize(okexFuturePosition.getHolding().get(i).getShort_avail_qty());
                    okexFutureRestAPI.OkexSetFutureOrder(closeShortOrder, okexAccount);
                }
            }
        } catch (Exception e) {
            logger.error(instrument_id + " " + okexAccount.getWxid() + ":initAccount,closeOrders," + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean setMargin(String currency, OkexAccount okexAccount) {

        try {
            OkexFutureMarginMode okexFutureMarginMode = new OkexFutureMarginMode();
            okexFutureMarginMode.setCurrency(currency);
            okexFutureMarginMode.setMargin_mode(Constants.MARGIN_MODE_FIXED);
            okexFutureRestAPI.OkexSetFutureMarginMode(okexFutureMarginMode, okexAccount);
        } catch (Exception e) {
            logger.error(currency + " " + okexAccount.getWxid() + ":initAccount,OkexSetFutureMarginMode," + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean setLeverage(String currency, String alias, String instrument_id, OkexAccount okexAccount) {

        OkexOrderRule orderRuleFindCondition = new OkexOrderRule();
        orderRuleFindCondition.setAlias(alias);
        orderRuleFindCondition.setCurrency(currency);
        OkexOrderRule okexOrderRule = orderRuleService.findOrderRuleByCurrencyAndAlias(orderRuleFindCondition);

        try {
            OkexFutureLeverage requestBody = new OkexFutureLeverage();
            requestBody.setLeverage(okexOrderRule.getLeverage());
            requestBody.setInstrument_id(instrument_id);
            requestBody.setDirection("long");
            requestBody.setCurrency(currency);
            okexFutureRestAPI.OkexSetFutureLeverage(requestBody, okexAccount);
        } catch (Exception e) {
            logger.error(instrument_id + " " + okexAccount.getWxid() + ":initAccount,setLongLeverage," + e.getMessage());
            e.printStackTrace();
            return false;
        }

        try {
            OkexFutureLeverage requestBody = new OkexFutureLeverage();
            requestBody.setLeverage(okexOrderRule.getLeverage());
            requestBody.setInstrument_id(instrument_id);
            requestBody.setDirection("short");
            requestBody.setCurrency(currency);
            okexFutureRestAPI.OkexSetFutureLeverage(requestBody, okexAccount);
        } catch (Exception e) {
            logger.error(instrument_id + " " + okexAccount.getWxid() + ":initAccount,setShortLeverage," + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
