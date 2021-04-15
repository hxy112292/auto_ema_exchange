package com.okex.auto.mapper;

import com.okex.auto.info.OkexAccount;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OkexAccountMapper {

    @SelectProvider(type = OkexAccountSQL.class, method = "findBySelective")
    public List<OkexAccount> findList(OkexAccount info);

    @SelectProvider(type = OkexAccountSQL.class, method = "findBySelective")
    public OkexAccount findOne(OkexAccount info);

    @UpdateProvider(type = OkexAccountSQL.class, method = "updateStatus")
    public int updateStatus(OkexAccount info);
}
