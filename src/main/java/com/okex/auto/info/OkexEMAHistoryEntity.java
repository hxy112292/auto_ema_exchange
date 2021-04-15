package com.okex.auto.info;

import java.util.Date;

public class OkexEMAHistoryEntity {

    private String uuid;

    private String currency;

    private String instrument_id;

    private String ema_low;

    private String ema_high;

    private Date timestamp;

    private String granularity;

    private String yesterday_cross;

    private String today_cross;

    private String alias;

    private String ema_rule_low;

    private String ema_rule_high;

    private Date create_time;

    private Date delete_time;

    private Date update_time;

    public String getEma_rule_low() {
        return ema_rule_low;
    }

    public void setEma_rule_low(String ema_rule_low) {
        this.ema_rule_low = ema_rule_low;
    }

    public String getEma_rule_high() {
        return ema_rule_high;
    }

    public void setEma_rule_high(String ema_rule_high) {
        this.ema_rule_high = ema_rule_high;
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

    public String getEma_low() {
        return ema_low;
    }

    public void setEma_low(String ema_low) {
        this.ema_low = ema_low;
    }

    public String getEma_high() {
        return ema_high;
    }

    public void setEma_high(String ema_high) {
        this.ema_high = ema_high;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getYesterday_cross() {
        return yesterday_cross;
    }

    public void setYesterday_cross(String yesterday_cross) {
        this.yesterday_cross = yesterday_cross;
    }

    public String getToday_cross() {
        return today_cross;
    }

    public void setToday_cross(String today_cross) {
        this.today_cross = today_cross;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }
}
