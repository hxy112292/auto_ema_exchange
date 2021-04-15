package com.okex.auto.info;

import java.util.List;

public class OkexSwapPosition {
    private String margin_mode;
    private List<OkexSwapPositionHolding> holding;

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public List<OkexSwapPositionHolding> getHolding() {
        return holding;
    }

    public void setHolding(List<OkexSwapPositionHolding> holding) {
        this.holding = holding;
    }
}
