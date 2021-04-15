package com.okex.auto.mapper;

import com.okex.auto.info.OkexEMAHistoryEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
public interface EMAHistoryMapper {

    @InsertProvider(type = EMAHistorySQL.class, method = "saveEMAHistory")
    public int saveEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity);

    @UpdateProvider(type = EMAHistorySQL.class, method = "updateEMAHistory")
    public int updateEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity);


    @Select("select * from ema_history where timestamp=(select MAX(timestamp) from ema_history where currency=#{currency} and alias=#{alias}) and currency=#{currency} and alias=#{alias} limit 1")
    public OkexEMAHistoryEntity findCurrentEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity);


    @Delete("delete from ema_history where currency=#{currency} and alias=#{alias}")
    public int clearEMAHistory(OkexEMAHistoryEntity okexEMAHistoryEntity);
}
