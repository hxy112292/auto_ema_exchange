package com.okex.auto.info;

import java.util.List;

public class OkexFutureOrderList {

    private String result;

    private List<OkexFutureOrderInfo> order_info;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<OkexFutureOrderInfo> getOrder_info() {
        return order_info;
    }

    public void setOrder_info(List<OkexFutureOrderInfo> order_info) {
        this.order_info = order_info;
    }
}
