package com.okex.auto.info;

public class OkexFutureTicker {

    public String last;
    public String best_ask;
    public String best_bid;
    public String high_24h;
    public String low_24h;
    public String volume_24h;
    public String timestamp;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getBest_ask() {
        return best_ask;
    }

    public void setBest_ask(String best_ask) {
        this.best_ask = best_ask;
    }

    public String getBest_bid() {
        return best_bid;
    }

    public void setBest_bid(String best_bid) {
        this.best_bid = best_bid;
    }

    public String getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(String high_24h) {
        this.high_24h = high_24h;
    }

    public String getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(String low_24h) {
        this.low_24h = low_24h;
    }

    public String getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(String volume_24h) {
        this.volume_24h = volume_24h;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
