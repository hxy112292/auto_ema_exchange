package com.okex.auto.info;

public class OkexFutureInstrument {

    private String instrument_id;

    private String underlying_index;

    private String quote_currency;

    private String contract_val;

    private String listing;

    private String delivery;

    private String tick_size;

    private String trade_increment;

    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getUnderlying_index() {
        return underlying_index;
    }

    public void setUnderlying_index(String underlying_index) {
        this.underlying_index = underlying_index;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public String getContract_val() {
        return contract_val;
    }

    public void setContract_val(String contract_val) {
        this.contract_val = contract_val;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTick_size() {
        return tick_size;
    }

    public void setTick_size(String tick_size) {
        this.tick_size = tick_size;
    }

    public String getTrade_increment() {
        return trade_increment;
    }

    public void setTrade_increment(String trade_increment) {
        this.trade_increment = trade_increment;
    }
}
