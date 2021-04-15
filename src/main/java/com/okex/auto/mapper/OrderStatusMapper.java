package com.okex.auto.mapper;

import com.okex.auto.info.OkexOrderStatus;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderStatusMapper {

    @UpdateProvider(type = OrderStatusSQL.class, method = "updateOrderStatus")
    public int updateOrderStatus(OkexOrderStatus info);

    @SelectProvider(type = OrderStatusSQL.class, method = "findOrderStatus")
    public OkexOrderStatus findOrderStatus(OkexOrderStatus info);

    @SelectProvider(type = OrderStatusSQL.class, method = "findOrderStatus")
    public List<OkexOrderStatus> findOrderStatusList(OkexOrderStatus info);
}
