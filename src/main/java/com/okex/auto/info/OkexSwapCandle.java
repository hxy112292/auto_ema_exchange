package com.okex.auto.info;

public class OkexSwapCandle {

    private String timestamp;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String currency_volume;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCurrency_volume() {
        return currency_volume;
    }

    public void setCurrency_volume(String currency_volume) {
        this.currency_volume = currency_volume;
    }
}
