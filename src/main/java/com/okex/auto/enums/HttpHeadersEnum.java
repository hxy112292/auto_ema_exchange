package com.okex.auto.enums;

public enum HttpHeadersEnum {

    OK_ASSET_ID("OK-ACCESS-KEY"),
    OK_ACCESS_SIGN("OK-ACCESS-SIGN"),
    OK_ACCESS_TIMESTAMP("OK-ACCESS-TIMESTAMP"),
    OK_PROJECT_ID("OK-ACCESS-PASSPHRASE"),

    OK_FROM("OK-FROM"),
    OK_TO("OK-TO"),
    OK_LIMIT("OK-LIMIT"),;

    private String header;

    HttpHeadersEnum(String header) {
        this.header = header;
    }

    public String header() {
        return header;
    }
}
