package com.okex.auto.mapper;

import com.mysql.cj.core.util.StringUtils;
import com.okex.auto.info.OkexEMAHistoryEntity;
import com.okex.auto.util.UUIDUtil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class EMAHistorySQL {

    public String updateEMAHistory(OkexEMAHistoryEntity info) {
        StringBuilder sb = new StringBuilder("update ema_history set ");
        if(!StringUtils.isNullOrEmpty(info.getInstrument_id())) {
            sb.append("instrument_id='" + info.getInstrument_id() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getEma_low())) {
            sb.append("ema_low='" + info.getEma_low() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getEma_high())) {
            sb.append("ema_high='" + info.getEma_high() + "',");
        }
        if(info.getTimestamp()!=null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDate.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String timestamp = simpleDate.format(info.getTimestamp());
            sb.append("timestamp='" + timestamp + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getYesterday_cross())) {
            sb.append("yesterday_cross='" + info.getYesterday_cross() + "',");
        }
        if(!StringUtils.isNullOrEmpty(info.getToday_cross())) {
            sb.append("today_cross='" + info.getToday_cross() + "',");
        }
        sb.append("update_time=now() where uuid='" + info.getUuid() + "'");
        return sb.toString();
    }

    public String saveEMAHistory(OkexEMAHistoryEntity info) {
        StringBuilder sb = new StringBuilder("insert into ema_history (uuid");
        if (!StringUtils.isNullOrEmpty(info.getInstrument_id())) {
            sb.append(",instrument_id");
        }

        if (!StringUtils.isNullOrEmpty(info.getCurrency())) {
            sb.append(",currency");
        }

        if (!StringUtils.isNullOrEmpty(info.getEma_low())) {
            sb.append(",ema_low");
        }

        if (!StringUtils.isNullOrEmpty(info.getEma_high())) {
            sb.append(",ema_high");
        }

        if (info.getTimestamp() != null) {
            sb.append(",timestamp");
        }

        if (!StringUtils.isNullOrEmpty(info.getGranularity())) {
            sb.append(",granularity");
        }

        if (!StringUtils.isNullOrEmpty(info.getYesterday_cross())) {
            sb.append(",yesterday_cross");
        }

        if (!StringUtils.isNullOrEmpty(info.getToday_cross())) {
            sb.append(",today_cross");
        }

        if (!StringUtils.isNullOrEmpty(info.getAlias())) {
            sb.append(",alias");
        }
        if (!StringUtils.isNullOrEmpty(info.getEma_rule_low())) {
            sb.append(",ema_rule_low");
        }
        if (!StringUtils.isNullOrEmpty(info.getEma_rule_high())) {
            sb.append(",ema_rule_high");
        }


        sb.append(") values (");
        if (!StringUtils.isNullOrEmpty(info.getUuid())) {
            sb.append("'" + info.getUuid() + "'");
        } else {
            sb.append("'" + UUIDUtil.getUUID() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getInstrument_id())) {
            sb.append(",'" + info.getInstrument_id() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getCurrency())) {
            sb.append(",'" + info.getCurrency() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getEma_low())) {
            sb.append(",'" + info.getEma_low() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getEma_high())) {
            sb.append(",'" + info.getEma_high() + "'");
        }

        if (info.getTimestamp() != null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDate.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String timestamp = simpleDate.format(info.getTimestamp());
            sb.append(",'" + timestamp + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getGranularity())) {
            sb.append(",'" + info.getGranularity() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getYesterday_cross())) {
            sb.append(",'" + info.getYesterday_cross() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getToday_cross())) {
            sb.append(",'" + info.getToday_cross() + "'");
        }

        if (!StringUtils.isNullOrEmpty(info.getAlias())) {
            sb.append(",'" + info.getAlias() + "'");
        }
        if (!StringUtils.isNullOrEmpty(info.getEma_rule_low())) {
            sb.append(",'" + info.getEma_rule_low() + "'");
        }
        if (!StringUtils.isNullOrEmpty(info.getEma_rule_high())) {
            sb.append(",'" + info.getEma_rule_high() + "'");
        }

        sb.append(")");
        return sb.toString();
    }
}
