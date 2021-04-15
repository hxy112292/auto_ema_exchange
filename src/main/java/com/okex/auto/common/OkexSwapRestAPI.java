package com.okex.auto.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.core.util.StringUtils;
import com.okex.auto.enums.ContentTypeEnum;
import com.okex.auto.enums.HttpHeadersEnum;
import com.okex.auto.info.*;
import com.okex.auto.util.HmacSHA256Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class OkexSwapRestAPI {

    @Value("${com.okex.auto.http.rootUrl}")
    String rootUrl;

    @Autowired
    RestTemplate restTemplate;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public OkexSwapPosition OkexGetSwapPosition(String instrument_id, OkexAccount okexAccount) {
        String requestUrl = "/api/swap/v3/" + instrument_id + "/position";
        String url = rootUrl + requestUrl;

        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.GET, requestUrl, null, null, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapPosition okexSwapPosition = new Gson().fromJson(result, new TypeToken<OkexSwapPosition>(){}.getType());
            return okexSwapPosition;
        } else {
            logger.error("OkexGetSwapPosition:{}", response.toString());
            return null;
        }
    }

    public String OkexGetSwapInstrumentID(String currency) {

        String url = rootUrl + "/api/swap/v3/instruments";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            List<OkexSwapInstrument> okexSwapInstrumentList = new Gson().fromJson(result, new TypeToken<List<OkexSwapInstrument>>(){}.getType());
            for(int i = 0; i< okexSwapInstrumentList.size(); i++) {
                if(okexSwapInstrumentList.get(i).getUnderlying_index().equals(currency)) {
                    return okexSwapInstrumentList.get(i).getInstrument_id();
                }
            }
            logger.error("OkexGetSwapInstrumentID:" + response.toString());
            return null;
        } else {
            logger.error("OkexGetSwapInstrumentID:" + response.toString());
            return null;
        }
    }

    public OkexSwapAccountCurrency OkexGetSwapAccountCurrency(String instrument_id, OkexAccount okexAccount) {

        String requestUrl = "/api/swap/v3/"+instrument_id+"/accounts";
        String url = rootUrl + requestUrl;

        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.GET, requestUrl, null, null, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapAccountCurrency okexSwapAccountCurrency = new Gson().fromJson(result, new TypeToken<OkexSwapAccountCurrency>(){}.getType());
            return okexSwapAccountCurrency;
        } else {
            logger.error("OkexGetSwapAccountCurrency" + response.toString());
            return null;
        }

    }

    public OkexSwapSetting OkexGetSwapSetting(String instrument_id, OkexAccount okexAccount) {

        String requestUrl = "/api/swap/v3/accounts/"+instrument_id+"/settings";
        String url = rootUrl + requestUrl;

        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.GET, requestUrl, null, null, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapSetting okexSwapSetting = new Gson().fromJson(result, new TypeToken<OkexSwapSetting>(){}.getType());
            return okexSwapSetting;
        } else {
            logger.error("OkexGetSwapSetting" + response.toString());
            return null;
        }

    }

    public OkexSwapSetting OkexSetSwapSetting(String instrument_id, OkexSwapSetting requestBody, OkexAccount okexAccount) {

        String requestUrl = "/api/swap/v3/accounts/"+instrument_id+"/leverage";
        String url = rootUrl + requestUrl;

        HttpHeaders headers = new HttpHeaders();

        String requestBodyStr = new Gson().toJson(requestBody);

        try {
            headers = getHeaders(HttpMethod.POST, requestUrl, null, requestBodyStr, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(requestBodyStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapSetting okexSwapSetting = new Gson().fromJson(result, new TypeToken<OkexSwapSetting>(){}.getType());
            return okexSwapSetting;
        } else {
            logger.error("OkexSetSwapSetting" + response.toString());
            return null;
        }

    }

    public OkexSwapTicker OkexGetSwapTicker(String instrument_id) {

        String url = rootUrl + "/api/swap/v3/instruments/" + instrument_id + "/ticker";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapTicker okexSwapTicker = new Gson().fromJson(result, new TypeToken<OkexSwapTicker>(){}.getType());
            return okexSwapTicker;
        } else {
            logger.error("OkexGetSwapTicker:" + response.toString());
            return null;
        }
    }

    public List<OkexSwapCandle> OkexGetSwapCandle(String instrument_id, String startTime, String endTime, String granularity) {

        String url = rootUrl + "/api/swap/v3/instruments/"+ instrument_id + "/candles?start={start}&end={end}&granularity={granularity}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("start",startTime);
        map.put("end",endTime);
        map.put("granularity",granularity);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();

            List<List<String >> noJsonList = new Gson().fromJson(result, new TypeToken<List<List<String>>>(){}.getType());;
            List<OkexSwapCandle> okexSwapCandles = new ArrayList<>();

            for(int i=0;i<noJsonList.size();i++) {
                OkexSwapCandle okexSwapCandle = new OkexSwapCandle();
                okexSwapCandle.setTimestamp(noJsonList.get(i).get(0));
                okexSwapCandle.setOpen(noJsonList.get(i).get(1));
                okexSwapCandle.setHigh(noJsonList.get(i).get(2));
                okexSwapCandle.setLow(noJsonList.get(i).get(3));
                okexSwapCandle.setClose(noJsonList.get(i).get(4));
                okexSwapCandle.setVolume(noJsonList.get(i).get(5));
                okexSwapCandle.setCurrency_volume(noJsonList.get(i).get(6));
                okexSwapCandles.add(okexSwapCandle);
            }
            return okexSwapCandles;
        } else {
            logger.error("OkexGetSwapCandle:" + response.toString());
            return null;
        }
    }

    public OkexSwapOrderList OkexGetSwapOrderList(String instrument_id, String status, OkexAccount okexAccount) {

        String requestUrl = "/api/swap/v3/orders/" + instrument_id + "?status=" + status;
        String url = rootUrl + requestUrl;

        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.GET, requestUrl, null, null, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        Map<String, String> map = new HashMap<>();
        map.put("status", status);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapOrderList okexSwapOrderList = new Gson().fromJson(result, new TypeToken<OkexSwapOrderList>(){}.getType());
            return okexSwapOrderList;
        } else {
            logger.error("OkexGetSwapOrderList" + response.toString());
            return null;
        }

    }

    public OkexSwapDepth OkexGetSwapDepth(String instrument_id, String size) {
        String url = rootUrl + "/api/swap/v3/instruments/" + instrument_id + "/depth?size={size}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("size",size);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexSwapDepth okexSwapDepth = new Gson().fromJson(result, new TypeToken<OkexSwapDepth>(){}.getType());
            return okexSwapDepth;
        } else {
            logger.error("OkexGetSwapDepth:" + response.toString());
            return null;
        }

    }

    public OkexSwapOrderInfo OkexSetSwapOrder(OkexSwapOrderInfo requestBody, OkexAccount okexAccount) {
        String requestUrl = "/api/swap/v3/order/";
        String url = rootUrl + requestUrl;

        String requestBodyStr = new Gson().toJson(requestBody);

        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.POST, requestUrl, null, requestBodyStr, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(requestBodyStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String resultStr = response.getBody();
            OkexSwapOrderInfo result = new Gson().fromJson(resultStr, new TypeToken<OkexSwapOrderInfo>(){}.getType());
            return result;
        } else {
            logger.error("OkexSetSwapOrder" + response.toString());
            return null;
        }

    }

    public OkexSwapOrderInfo OkexCancelSwapOrder(OkexSwapOrderInfo okexSwapOrderInfo, OkexAccount okexAccount) {
        String requestUrl = new String();

        if(!StringUtils.isNullOrEmpty(okexSwapOrderInfo.getOrder_id())) {
            requestUrl = "/api/swap/v3/cancel_order/" + okexSwapOrderInfo.getInstrument_id() + "/" + okexSwapOrderInfo.getOrder_id();
        } else if(!StringUtils.isNullOrEmpty(okexSwapOrderInfo.getClient_oid())){
            requestUrl = "/api/swap/v3/cancel_order/" + okexSwapOrderInfo.getInstrument_id() + "/" + okexSwapOrderInfo.getClient_oid();
        } else {
            logger.error("OkexCancelSwapOrder" + ":orderId or clientOid must have a value");
            return null;
        }
        String url = rootUrl + requestUrl;

        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.POST, requestUrl, null, null, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String resultStr = response.getBody();
            OkexSwapOrderInfo result = new Gson().fromJson(resultStr, new TypeToken<OkexSwapOrderInfo>(){}.getType());
            return result;
        } else {
            logger.error("OkexCancelSwapOrder" + response.toString());
            return null;
        }
    }

    public HttpHeaders getHeaders(HttpMethod method, String requestUrl, String queryString, String requestBody, OkexAccount okexAccount) throws Exception{
        SimpleDateFormat requestSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        requestSdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String timestamp = requestSdf.format(new Date());
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeadersEnum.OK_ASSET_ID.header(), okexAccount.getAccess_key());
        headers.add(HttpHeadersEnum.OK_ACCESS_TIMESTAMP.header(), timestamp);
        headers.add(HttpHeadersEnum.OK_PROJECT_ID.header(), okexAccount.getPhrase());
        headers.add("Content-Type", ContentTypeEnum.APPLICATION_JSON.contentType());

        String sign = HmacSHA256Base64Utils.sign(timestamp, method.toString(), requestUrl, queryString, requestBody, okexAccount.getS_key());

        headers.add(HttpHeadersEnum.OK_ACCESS_SIGN.header(), sign);

        return headers;
    }
}
