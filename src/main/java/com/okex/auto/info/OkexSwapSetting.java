package com.okex.auto.info;

public class OkexSwapSetting {

    private String long_leverage;
    private String margin_mode;
    private String short_leverage;
    private String instrument_id;
    private String leverage;
    private String side;

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getLong_leverage() {
        return long_leverage;
    }

    public void setLong_leverage(String long_leverage) {
        this.long_leverage = long_leverage;
    }

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public String getShort_leverage() {
        return short_leverage;
    }

    public void setShort_leverage(String short_leverage) {
        this.short_leverage = short_leverage;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }
}
