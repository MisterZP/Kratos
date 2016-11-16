package com.kratos.config;

import org.apache.qpid.client.AMQAnyDestination;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.jms.ConnectionFactory;
import javax.naming.NamingException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

/**
 * Created by zengping on 2016/11/2.
 */
@Configuration
public class CommonConfig {

    private final String[] qpidKeys = {"java.naming.factory.initial",
                "destination.topicExchange",
                "connectionfactory.qpidConnectionfactory"};

    @Autowired
    Environment enviroment;
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

    @Bean
    public JndiTemplate jndiTemplate() throws IOException {
        JndiTemplate jndiTemplate = new JndiTemplate();
        Properties properties = new Properties();
        for (String key: qpidKeys) {
            properties.put(key, enviroment.getProperty(key));
        }
        jndiTemplate.setEnvironment(properties);
        return jndiTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate(JndiTemplate jndiTemplate) throws IOException, NamingException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory((ConnectionFactory) jndiTemplate.lookup("qpidConnectionfactory"));
        cachingConnectionFactory.setSessionCacheSize(5);
        jmsTemplate.setConnectionFactory(cachingConnectionFactory);
        jmsTemplate.setDefaultDestination(((AMQAnyDestination) jndiTemplate.lookup("topicExchange")));
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    @Bean
    public JndiDestinationResolver destinationResolver(JndiTemplate jndiTemplate) throws IOException {
        JndiDestinationResolver jndiDestinationResolver = new JndiDestinationResolver();
        jndiDestinationResolver.setJndiTemplate(jndiTemplate);
        return jndiDestinationResolver;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(JndiDestinationResolver destinationResolver) throws IOException, NamingException {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory((ConnectionFactory)destinationResolver.getJndiTemplate().lookup("qpidConnectionfactory"));
        cachingConnectionFactory.setSessionCacheSize(5);
        defaultJmsListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
        defaultJmsListenerContainerFactory.setDestinationResolver(destinationResolver);
        defaultJmsListenerContainerFactory.setConcurrency("3-10");
        return defaultJmsListenerContainerFactory;
    }
}
