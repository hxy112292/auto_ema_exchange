package com.okex.auto.resttemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;


@Configuration
public class RestConfiguration {
    @Value("${proxy.enabled}")
    private boolean enabled;
    @Value("${proxy.host}")
    private String host;
    @Value("${proxy.port}")
    private Integer port;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        if(enabled) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            requestFactory.setProxy(proxy);
        }

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));

        return restTemplate;
    }
}
