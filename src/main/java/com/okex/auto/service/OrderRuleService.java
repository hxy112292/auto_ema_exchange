package com.okex.auto.service;

import com.okex.auto.info.OkexOrderRule;
import com.okex.auto.mapper.OrderRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRuleService {

    @Autowired
    OrderRuleMapper orderRuleMapper;

    public OkexOrderRule findOrderRuleByCurrencyAndAlias(OkexOrderRule okexOrderRule) {
        return orderRuleMapper.findOrderRuleByCurrencyAndAlias(okexOrderRule);
    }
}
