package com.okex.auto.info;

import java.util.List;

public class OkexFuturePosition {

    private String result;

    private List<OkexFuturePositionHolding> holding;

    private String margin_mode;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<OkexFuturePositionHolding> getHolding() {
        return holding;
    }

    public void setHolding(List<OkexFuturePositionHolding> holding) {
        this.holding = holding;
    }

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }
}
