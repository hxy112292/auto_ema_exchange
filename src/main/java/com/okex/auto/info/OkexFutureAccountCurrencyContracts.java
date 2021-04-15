package com.okex.auto.info;

public class OkexFutureAccountCurrencyContracts {

    private String available_qty;
    private String fixed_balance;
    private String instrument_id;
    private String margin_for_unfilled;
    private String margin_frozen;
    private String realized_pnl;
    private String currency;
    private String unrealized_pnl;

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

    public String getFixed_balance() {
        return fixed_balance;
    }

    public void setFixed_balance(String fixed_balance) {
        this.fixed_balance = fixed_balance;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getMargin_for_unfilled() {
        return margin_for_unfilled;
    }

    public void setMargin_for_unfilled(String margin_for_unfilled) {
        this.margin_for_unfilled = margin_for_unfilled;
    }

    public String getMargin_frozen() {
        return margin_frozen;
    }

    public void setMargin_frozen(String margin_frozen) {
        this.margin_frozen = margin_frozen;
    }

    public String getRealized_pnl() {
        return realized_pnl;
    }

    public void setRealized_pnl(String realized_pnl) {
        this.realized_pnl = realized_pnl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUnrealized_pnl() {
        return unrealized_pnl;
    }

    public void setUnrealized_pnl(String unrealized_pnl) {
        this.unrealized_pnl = unrealized_pnl;
    }
}
