package com.okex.auto.info;

import java.util.Date;

public class OkexOrderStatus {

    private String uuid;
    private String okex_id;
    private String currency;
    private String alias;
    private String order_init_status;
    private String order_set_status;
    private String order_hedging_status;
    private String order_stop_profit_status;
    private String order_stop_loss_status;
    private Date create_time;
    private Date delete_time;
    private Date update_time;

    public String getOrder_stop_profit_status() {
        return order_stop_profit_status;
    }

    public void setOrder_stop_profit_status(String order_stop_profit_status) {
        this.order_stop_profit_status = order_stop_profit_status;
    }

    public String getOrder_stop_loss_status() {
        return order_stop_loss_status;
    }

    public void setOrder_stop_loss_status(String order_stop_loss_status) {
        this.order_stop_loss_status = order_stop_loss_status;
    }

    public String getOrder_hedging_status() {
        return order_hedging_status;
    }

    public void setOrder_hedging_status(String order_hedging_status) {
        this.order_hedging_status = order_hedging_status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Date delete_time) {
        this.delete_time = delete_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOkex_id() {
        return okex_id;
    }

    public void setOkex_id(String okex_id) {
        this.okex_id = okex_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOrder_init_status() {
        return order_init_status;
    }

    public void setOrder_init_status(String order_init_status) {
        this.order_init_status = order_init_status;
    }

    public String getOrder_set_status() {
        return order_set_status;
    }

    public void setOrder_set_status(String order_set_status) {
        this.order_set_status = order_set_status;
    }
}
