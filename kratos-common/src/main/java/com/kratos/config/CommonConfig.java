package com.kratos.config;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by zengping on 2016/11/2.
 */
@Configuration
public class CommonConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter: converters) {
            if(converter instanceof StringHttpMessageConverter){
                converters.remove(converter);
                HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
                converters.add(stringHttpMessageConverter);
                break;
            }
        }
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        BeanUtils.copyProperties(restTemplate.getRequestFactory(), requestFactory);
        requestFactory.setReadTimeout(10 * 1000);//读写超时时间
        requestFactory.setConnectTimeout(10 * 1000);//连接超时时间
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        registrationBean.setFilter(characterEncodingFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
