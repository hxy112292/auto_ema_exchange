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
public class OkexFutureRestAPI {

    @Value("${com.okex.auto.http.rootUrl}")
    String rootUrl;

    @Autowired
    RestTemplate restTemplate;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public OkexTimeStamp OkexGetGeneralTime() {

        String url = rootUrl + "/api/general/v3/time";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexTimeStamp okexTimeStampInfo = new Gson().fromJson(result, new TypeToken<OkexTimeStamp>(){}.getType());
            return okexTimeStampInfo;
        } else {
            logger.error("OkexGetGeneralTime:" + response.toString());
            return null;
        }
    }

    public List<OkexFutureCandle> OkexGetFutureCandle(String instrument_id, String startTime, String endTime, String granularity) {

        String url = rootUrl + "/api/futures/v3/instruments/"+ instrument_id + "/candles?start={start}&end={end}&granularity={granularity}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("start",startTime);
        map.put("end",endTime);
        map.put("granularity",granularity);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();

            List<List<String >> noJsonList = new Gson().fromJson(result, new TypeToken<List<List<String>>>(){}.getType());;
            List<OkexFutureCandle> okexFutureCandles = new ArrayList<>();

            for(int i=0;i<noJsonList.size();i++) {
                OkexFutureCandle okexFutureCandle = new OkexFutureCandle();
                okexFutureCandle.setTimestamp(noJsonList.get(i).get(0));
                okexFutureCandle.setOpen(noJsonList.get(i).get(1));
                okexFutureCandle.setHigh(noJsonList.get(i).get(2));
                okexFutureCandle.setLow(noJsonList.get(i).get(3));
                okexFutureCandle.setClose(noJsonList.get(i).get(4));
                okexFutureCandle.setVolume(noJsonList.get(i).get(5));
                okexFutureCandle.setCurrency_volume(noJsonList.get(i).get(6));
                okexFutureCandles.add(okexFutureCandle);
            }
            return okexFutureCandles;
        } else {
            return null;
        }
    }

    public List<OkexFutureInstrument> OkexGetFutureInstruments() {

        String url = rootUrl + "/api/futures/v3/instruments";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            List<OkexFutureInstrument> okexFutureInstruments = new Gson().fromJson(result, new TypeToken<List<OkexFutureInstrument>>(){}.getType());
            return okexFutureInstruments;
        } else {
            logger.error("OkexGetFutureInstruments:" + response.toString());
            return null;
        }
    }

    public String OkexGetFutureInstrumentID(String currency, String alias) {

        String url = rootUrl + "/api/futures/v3/instruments";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            List<OkexFutureInstrument> okexFutureInstruments = new Gson().fromJson(result, new TypeToken<List<OkexFutureInstrument>>(){}.getType());
            for(int i = 0; i< okexFutureInstruments.size(); i++) {
                if(okexFutureInstruments.get(i).getAlias().equals(alias) && okexFutureInstruments.get(i).getUnderlying_index().equals(currency)) {
                    return okexFutureInstruments.get(i).getInstrument_id();
                }
            }
            logger.error("OkexGetFutureInstrumentID:" + response.toString());
            return null;
        } else {
            logger.error("OkexGetFutureInstrumentID:" + response.toString());
            return null;
        }
    }

    public OkexFutureTicker OkexGetfutureticker(String instrument_id) {

        String url = rootUrl + "/api/futures/v3/instruments/" + instrument_id + "/ticker";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexFutureTicker okexFutureTicker = new Gson().fromJson(result, new TypeToken<OkexFutureTicker>(){}.getType());
            return okexFutureTicker;
        } else {
            logger.error("OkexGetfutureticker:" + response.toString());
            return null;
        }
    }

    public OkexFuturePosition OkexGetFuturePosition(String instrument_id, OkexAccount okexAccount) {

        String requestUrl = "/api/futures/v3/" + instrument_id + "/position";
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
            OkexFuturePosition okexFuturePosition = new Gson().fromJson(result, new TypeToken<OkexFuturePosition>(){}.getType());
            return okexFuturePosition;
        } else {
            logger.error("OkexGetFuturePosition:{}", response.toString());
            return null;
        }

    }

    public OkexFutureAccountCurrency OkexGetFutureAccountCurrency(String currency, OkexAccount okexAccount) {

        String requestUrl = "/api/futures/v3/accounts/" + currency;
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
            OkexFutureAccountCurrency okexFutureAccountCurrency = new Gson().fromJson(result, new TypeToken<OkexFutureAccountCurrency>(){}.getType());
            return okexFutureAccountCurrency;
        } else {
            logger.error("OkexGetAccountCurrency" + response.toString());
            return null;
        }

    }

    public OkexFutureOrderList OkexGetFutureOrderList(String instrument_id, String status, OkexAccount okexAccount) {

        String requestUrl = "/api/futures/v3/orders/" + instrument_id + "?status=" + status;
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
            OkexFutureOrderList okexFutureOrderList = new Gson().fromJson(result, new TypeToken<OkexFutureOrderList>(){}.getType());
            return okexFutureOrderList;
        } else {
            logger.error("OkexFutureGetOrderList" + response.toString());
            return null;
        }

    }

    public OkexFutureOrderInfo OkexGetFutureOrderByClientOid(String instrumentId, String client_oid, OkexAccount okexAccount) {
        String requestUrl = "/api/futures/v3/orders/" + instrumentId + "/" + client_oid;
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
            if(result.isEmpty() || result.equals("{}") || result.equals("") || result == null)
                return null;
            OkexFutureOrderInfo info = new Gson().fromJson(result, new TypeToken<OkexFutureOrderInfo>(){}.getType());
            return info;
        } else {
            logger.error("OkexFutureGetOrderList" + response.toString());
            return null;
        }
    }

    public String OkexSetFutureLeverage(OkexFutureLeverage requestBody, OkexAccount okexAccount) {
        String requestUrl = "/api/futures/v3/accounts/" + requestBody.getCurrency() + "/leverage";
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
            String result = response.getBody();
            //OkexFutureLeverage okexFutureLeverage = new Gson().fromJson(result, new TypeToken<OkexFutureLeverage>(){}.getType());
            return result;
        } else {
            logger.error("OkexSetFutureLeverage" + response.toString());
            return null;
        }

    }

    public OkexFutureOrderInfo OkexSetFutureOrder(OkexFutureOrderInfo requestBody, OkexAccount okexAccount) {
        String requestUrl = "/api/futures/v3/order/";
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
            OkexFutureOrderInfo result = new Gson().fromJson(resultStr, new TypeToken<OkexFutureOrderInfo>(){}.getType());
            return result;
        } else {
            logger.error("OkexSetFutureOrder" + response.toString());
            return null;
        }

    }

    public OkexFutureOrderInfo OkexCancelFutureOrder(OkexFutureOrderInfo okexFutureOrderInfo, OkexAccount okexAccount) {
        String requestUrl = new String();

        if(!StringUtils.isNullOrEmpty(okexFutureOrderInfo.getOrder_id())) {
            requestUrl = "/api/futures/v3/cancel_order/" + okexFutureOrderInfo.getInstrument_id() + "/" + okexFutureOrderInfo.getOrder_id();
        } else if(!StringUtils.isNullOrEmpty(okexFutureOrderInfo.getClient_oid())){
            requestUrl = "/api/futures/v3/cancel_order/" + okexFutureOrderInfo.getInstrument_id() + "/" + okexFutureOrderInfo.getClient_oid();
        } else {
            logger.error("OkexCancelFutureOrder" + ":orderId or clientOid must have a value");
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
            OkexFutureOrderInfo result = new Gson().fromJson(resultStr, new TypeToken<OkexFutureOrderInfo>(){}.getType());
            return result;
        } else {
            logger.error("OkexCancelFutureOrder" + response.toString());
            return null;
        }
    }

    public OkexFutureBooks OkexGetFutureBooks(String instrument_id, String size) {
        String url = rootUrl + "/api/futures/v3/instruments/" + instrument_id + "/book?size={size}";

        Map<String,String> map=new HashMap<String,String>();
        map.put("size",size);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            OkexFutureBooks okexFutureBooks = new Gson().fromJson(result, new TypeToken<OkexFutureBooks>(){}.getType());
            return okexFutureBooks;
        } else {
            logger.error("OkexGetFutureBooks:" + response.toString());
            return null;
        }

    }

    public OkexFutureMarginMode OkexSetFutureMarginMode(OkexFutureMarginMode requestBody, OkexAccount okexAccount) {
        String requestUrl = "/api/futures/v3/accounts/margin_mode";
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
            OkexFutureMarginMode result = new Gson().fromJson(resultStr, new TypeToken<OkexFutureMarginMode>(){}.getType());
            return result;
        } else {
            logger.error("OkexSetFutureMarginMode" + response.toString());
            return null;
        }

    }

    public String OkexGetFutureLeverage(String currency, OkexAccount okexAccount) {
        String requestUrl = "/api/futures/v3/accounts/" + currency + "/leverage";

        String url = rootUrl + requestUrl;


        HttpHeaders headers = new HttpHeaders();

        try {
            headers = getHeaders(HttpMethod.GET, requestUrl, null, null, okexAccount);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new HashMap<>());

        if(response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            return result;
        } else {
            logger.error("OkexGetFutureBooks:" + response.toString());
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
        headers.add("Content-Type",ContentTypeEnum.APPLICATION_JSON.contentType());

        String sign = HmacSHA256Base64Utils.sign(timestamp, method.toString(), requestUrl, queryString, requestBody, okexAccount.getS_key());

        headers.add(HttpHeadersEnum.OK_ACCESS_SIGN.header(), sign);

        return headers;
    }

}
