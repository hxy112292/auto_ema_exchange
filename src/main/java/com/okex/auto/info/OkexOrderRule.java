package com.okex.auto.info;

public class OkexOrderRule {

    private String uuid;
    private String currency;
    private String alias;
    private Float stop_profit_ratio;
    private Float stop_profit_size;
    private Float hedging_ratio;
    private Float hedging_size;
    private Float stop_loss_ratio;
    private Float stop_loss_size;
    private Float position_size;
    private Integer deep_size;
    private String leverage;

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public Float getPosition_size() {
        return position_size;
    }

    public void setPosition_size(Float position_size) {
        this.position_size = position_size;
    }

    public Integer getDeep_size() {
        return deep_size;
    }

    public void setDeep_size(Integer deep_size) {
        this.deep_size = deep_size;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Float getStop_profit_ratio() {
        return stop_profit_ratio;
    }

    public void setStop_profit_ratio(Float stop_profit_ratio) {
        this.stop_profit_ratio = stop_profit_ratio;
    }

    public Float getStop_profit_size() {
        return stop_profit_size;
    }

    public void setStop_profit_size(Float stop_profit_size) {
        this.stop_profit_size = stop_profit_size;
    }

    public Float getHedging_ratio() {
        return hedging_ratio;
    }

    public void setHedging_ratio(Float hedging_ratio) {
        this.hedging_ratio = hedging_ratio;
    }

    public Float getHedging_size() {
        return hedging_size;
    }

    public void setHedging_size(Float hedging_size) {
        this.hedging_size = hedging_size;
    }

    public Float getStop_loss_ratio() {
        return stop_loss_ratio;
    }

    public void setStop_loss_ratio(Float stop_loss_ratio) {
        this.stop_loss_ratio = stop_loss_ratio;
    }

    public Float getStop_loss_size() {
        return stop_loss_size;
    }

    public void setStop_loss_size(Float stop_loss_size) {
        this.stop_loss_size = stop_loss_size;
    }
}
