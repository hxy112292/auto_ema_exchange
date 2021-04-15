package com.okex.auto.info;

public class OkexSwapInstrument {

    private String instrument_id;
    private String underlying_index;
    private String quote_currency;
    private String coin;
    private String contract_val;
    private String Listing;
    private String delivery;
    private String size_increment;
    private String tick_size;

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

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getContract_val() {
        return contract_val;
    }

    public void setContract_val(String contract_val) {
        this.contract_val = contract_val;
    }

    public String getListing() {
        return Listing;
    }

    public void setListing(String listing) {
        Listing = listing;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getSize_increment() {
        return size_increment;
    }

    public void setSize_increment(String size_increment) {
        this.size_increment = size_increment;
    }

    public String getTick_size() {
        return tick_size;
    }

    public void setTick_size(String tick_size) {
        this.tick_size = tick_size;
    }
}
