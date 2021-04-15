package com.okex.auto.info;

import java.util.List;

public class OkexSwapDepth {

    private List<List<Number>> bids;

    private List<List<Number>> asks;

    private String time;

    public List<List<Number>> getBids() {
        return bids;
    }

    public void setBids(List<List<Number>> bids) {
        this.bids = bids;
    }

    public List<List<Number>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<Number>> asks) {
        this.asks = asks;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
