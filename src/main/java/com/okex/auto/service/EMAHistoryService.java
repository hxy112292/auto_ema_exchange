package com.okex.auto.service;

import com.okex.auto.info.OkexEMAHistoryEntity;
import com.okex.auto.mapper.EMAHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EMAHistoryService {

    @Autowired
    private EMAHistoryMapper emaHistoryMapper;

    public int saveEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity) {
        return emaHistoryMapper.saveEMAHistory(okexEMAHistoryEntity);
    }

    public int updateEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity) {
        return emaHistoryMapper.updateEMAHistory(okexEMAHistoryEntity);
    }

    public OkexEMAHistoryEntity findCurrentEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity) {
        return emaHistoryMapper.findCurrentEMAHistory(okexEMAHistoryEntity);
    }

    public int clearEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity) {
        return emaHistoryMapper.clearEMAHistory(okexEMAHistoryEntity);
    }
}
