package com.okex.auto.info;

import java.util.Date;

public class OkexFutureOrderInfo {

    private String instrument_id;
    private String client_oid;
    private String size;
    private String timestamp;
    private String filled_qty;
    private String fee;
    private String order_id;
    private String price;
    private String price_avg;
    private String order_type;
    private String status;
    private String type;
    private String contract_val;
    private String leverage;
    private String pnl;
    private String match_price;
    private String error_code;
    private String error_message;
    private String result;
    private Date create_time;

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMatch_price() {
        return match_price;
    }

    public void setMatch_price(String match_price) {
        this.match_price = match_price;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFilled_qty() {
        return filled_qty;
    }

    public void setFilled_qty(String filled_qty) {
        this.filled_qty = filled_qty;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_avg() {
        return price_avg;
    }

    public void setPrice_avg(String price_avg) {
        this.price_avg = price_avg;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContract_val() {
        return contract_val;
    }

    public void setContract_val(String contract_val) {
        this.contract_val = contract_val;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getPnl() {
        return pnl;
    }

    public void setPnl(String pnl) {
        this.pnl = pnl;
    }
}
