package com.okex.auto.info;

import java.util.List;

public class OkexFutureAccountCurrency {

    private String margin_mode;
    private String total_avail_balance;
    private String equity;
    private String auto_margin;
    private List<OkexFutureAccountCurrencyContracts> contracts;

    public String getAuto_margin() {
        return auto_margin;
    }

    public void setAuto_margin(String auto_margin) {
        this.auto_margin = auto_margin;
    }

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public String getTotal_avail_balance() {
        return total_avail_balance;
    }

    public void setTotal_avail_balance(String total_avail_balance) {
        this.total_avail_balance = total_avail_balance;
    }

    public String getEquity() {
        return equity;
    }

    public void setEquity(String equity) {
        this.equity = equity;
    }

    public List<OkexFutureAccountCurrencyContracts> getContracts() {
        return contracts;
    }

    public void setContracts(List<OkexFutureAccountCurrencyContracts> contracts) {
        this.contracts = contracts;
    }
}
