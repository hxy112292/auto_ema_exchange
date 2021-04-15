package com.okex.auto.mapper;

import com.mysql.cj.core.util.StringUtils;
import com.okex.auto.info.OkexAccount;

public class OkexAccountSQL {
    public String findBySelective(OkexAccount info) {
        StringBuilder sb = new StringBuilder("select * from okex_account where 1=1 ");
        if(!StringUtils.isNullOrEmpty(info.getWxid())) {
            sb.append("and wxid='" + info.getWxid() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getWxname())) {
            sb.append("and wxname='" + info.getWxname() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getUuid())) {
            sb.append("and uuid='" + info.getUuid() + "' ");
        }
        if(!StringUtils.isNullOrEmpty(info.getAccount_status())) {
            sb.append("and account_status='" + info.getAccount_status() + "' ");
        }
        return sb.toString();
    }

    public String updateStatus(OkexAccount info) {
        StringBuilder sb = new StringBuilder("update okex_account set ");
        if(!StringUtils.isNullOrEmpty(info.getAccount_status())) {
            sb.append("account_status='" + info.getAccount_status() + "',");
        }
        sb.append("update_time=now() ");
        sb.append("where uuid='" + info.getUuid() + "'");
        return sb.toString();
    }
}
