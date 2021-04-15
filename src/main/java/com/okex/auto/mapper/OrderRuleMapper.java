package com.okex.auto.mapper;

import com.okex.auto.info.OkexOrderRule;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface OrderRuleMapper {

    @Select("select * from order_rule where currency=#{currency} and alias=#{alias}")
    public OkexOrderRule findOrderRuleByCurrencyAndAlias(OkexOrderRule okexOrderRule);
}
