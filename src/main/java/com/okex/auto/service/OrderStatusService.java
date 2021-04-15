package com.okex.auto.service;

import com.okex.auto.info.OkexOrderStatus;
import com.okex.auto.mapper.OrderStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusService {

    @Autowired
    OrderStatusMapper orderStatusMapper;

    public int updateOrderStatus(OkexOrderStatus info) {
        return orderStatusMapper.updateOrderStatus(info);
    }

    public OkexOrderStatus findOrderStatus(OkexOrderStatus info) {
        return orderStatusMapper.findOrderStatus(info);
    }

    public List<OkexOrderStatus> findOrderStatusList(OkexOrderStatus info) {
        return orderStatusMapper.findOrderStatusList(info);
    }
}
