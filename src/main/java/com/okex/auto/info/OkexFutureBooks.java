package com.okex.auto.info;

import java.util.List;

public class OkexFutureBooks {

    private List<List<Number>> bids;

    private List<List<Number>> asks;

    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
