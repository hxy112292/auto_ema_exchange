package com.okex.auto.common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class EMACrossCal {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public BigDecimal emaCal(BigDecimal N, BigDecimal yesterdayEmaPrice, BigDecimal todayPrice) {

        BigDecimal todayEmaPrice = yesterdayEmaPrice.add(new BigDecimal(2)
                .multiply(todayPrice.subtract(yesterdayEmaPrice)).divide(N.add(new BigDecimal(1)), 8, RoundingMode.HALF_UP));

        return  todayEmaPrice;
    }
}
