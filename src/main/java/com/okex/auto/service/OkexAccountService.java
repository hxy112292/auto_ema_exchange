package com.okex.auto.service;

import com.mysql.cj.core.util.StringUtils;
import com.okex.auto.info.OkexAccount;
import com.okex.auto.mapper.OkexAccountMapper;
import com.okex.auto.util.ContactUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class OkexAccountService {

    @Autowired
    public OkexAccountMapper okexAccountMapper;

    public List<OkexAccount> findList(OkexAccount info) {

        List<OkexAccount> result = okexAccountMapper.findList(info);
        List<OkexAccount> response = new ArrayList<>();
        for(int i=0;i<result.size();i++) {
            OkexAccount enInfo = result.get(i);
            OkexAccount deInfo = new OkexAccount();

            String wxid = enInfo.getWxid();
            if(StringUtils.isNullOrEmpty(wxid))
                wxid = "null";
            byte[] wxid64 = Base64.getEncoder().encode(wxid.getBytes());
            String wxname = enInfo.getWxname();
            if(StringUtils.isNullOrEmpty(wxname))
                wxname = "null";
            byte[] wxname64 = Base64.getEncoder().encode(wxname.getBytes());

            byte[] number = new byte[24];
            for(int j=0;j<24;j++) {
                number[j] = (byte)0xEF;
            }
            for(int j=0;j<(wxid64.length>12?12:wxid64.length);j++) {
                number[j] &= wxid64[j];
            }
            for(int j=12;j<(wxname64.length>24?24:wxname64.length);j++) {
                number[23-j] &= wxname64[j];
            }

            byte[] enAsset = ContactUtil.hex2byte(enInfo.getAccess_key());
            byte[] enStatic = ContactUtil.hex2byte(enInfo.getS_key());
            byte[] enProject = ContactUtil.hex2byte(enInfo.getPhrase());

            String deAsset = new String(ContactUtil.filish(number, enAsset));
            String deStatic = new String(ContactUtil.filish(number, enStatic));
            String deProject = new String(ContactUtil.filish(number, enProject));

            deInfo.setAccount_status(enInfo.getAccount_status());
            deInfo.setWxid(wxid);
            deInfo.setWxname(wxname);
            deInfo.setAccess_key(deAsset);
            deInfo.setS_key(deStatic);
            deInfo.setPhrase(deProject);
            deInfo.setUuid(enInfo.getUuid());

            response.add(deInfo);
        }

        return response;
    }

    public OkexAccount findOne(OkexAccount info) {
        OkexAccount enInfo = okexAccountMapper.findOne(info);
        OkexAccount deInfo = new OkexAccount();

        String wxid = enInfo.getWxid();
        if(StringUtils.isNullOrEmpty(wxid))
            wxid = "null";
        byte[] wxid64 = Base64.getEncoder().encode(wxid.getBytes());
        String wxname = enInfo.getWxname();
        if(StringUtils.isNullOrEmpty(wxname))
            wxname = "null";
        byte[] wxname64 = Base64.getEncoder().encode(wxname.getBytes());

        byte[] number = new byte[24];
        for(int j=0;j<24;j++) {
            number[j] = (byte)0xEF;
        }
        for(int j=0;j<(wxid64.length>12?12:wxid64.length);j++) {
            number[j] &= wxid64[j];
        }
        for(int j=12;j<(wxname64.length>24?24:wxname64.length);j++) {
            number[23-j] &= wxname64[j];
        }

        byte[] enAsset = ContactUtil.hex2byte(enInfo.getAccess_key());
        byte[] enStatic = ContactUtil.hex2byte(enInfo.getS_key());
        byte[] enProject = ContactUtil.hex2byte(enInfo.getPhrase());

        String deAsset = new String(ContactUtil.filish(number, enAsset));
        String deStatic = new String(ContactUtil.filish(number, enStatic));
        String deProject = new String(ContactUtil.filish(number, enProject));

        deInfo.setAccount_status(enInfo.getAccount_status());
        deInfo.setWxid(wxid);
        deInfo.setWxname(wxname);
        deInfo.setAccess_key(deAsset);
        deInfo.setS_key(deStatic);
        deInfo.setPhrase(deProject);
        deInfo.setUuid(enInfo.getUuid());

        return deInfo;
    }

    public int updateStatus(OkexAccount okexAccount) {
        return okexAccountMapper.updateStatus(okexAccount);
    }
}
