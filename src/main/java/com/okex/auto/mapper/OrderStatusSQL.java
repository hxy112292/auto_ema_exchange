package com.okex.auto.mapper;


import com.mysql.cj.core.util.StringUtils;
import com.okex.auto.info.OkexOrderStatus;

public class OrderStatusSQL {

    public String updateOrderStatus(OkexOrderStatus info) {
        StringBuilder sb = new StringBuilder("update order_status set ");
        if(!StringUtils.isNullOrEmpty(info.getOrder_init_status())) {
            sb.append("order_init_status='" + info.getOrder_init_status() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getOrder_set_status())) {
            sb.append("order_set_status='" + info.getOrder_set_status() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getOrder_hedging_status())) {
            sb.append("order_hedging_status='" + info.getOrder_hedging_status() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getOrder_stop_loss_status())) {
            sb.append("order_stop_loss_status='" + info.getOrder_stop_loss_status() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getOrder_stop_profit_status())) {
            sb.append("order_stop_profit_status='" + info.getOrder_stop_profit_status() + "',");
        }
        sb.append("update_time=now() where 1=1 ");
        if(!StringUtils.isNullOrEmpty(info.getUuid())) {
            sb.append("and uuid='" + info.getUuid() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getOkex_id())) {
            sb.append("and okex_id='" + info.getOkex_id() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getCurrency())) {
            sb.append("and currency='" + info.getCurrency() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getAlias())) {
            sb.append("and alias='" + info.getAlias() + "' ");
        }

        return sb.toString();
    }

    public String findOrderStatus(OkexOrderStatus info) {
        StringBuilder sb = new StringBuilder("select * from order_status where 1=1 ");
        if(!StringUtils.isNullOrEmpty(info.getUuid())) {
            sb.append("and uuid='" + info.getUuid() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getOkex_id())) {
            sb.append("and okex_id='" + info.getOkex_id() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getCurrency())) {
            sb.append("and currency='" + info.getCurrency() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getAlias())) {
            sb.append("and alias='" + info.getAlias() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getOrder_init_status())) {
            sb.append("and order_init_status='" + info.getOrder_init_status() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getOrder_set_status())) {
            sb.append("and order_set_status='" + info.getOrder_set_status() + "' ");
        }

        return sb.toString();
    }
}
