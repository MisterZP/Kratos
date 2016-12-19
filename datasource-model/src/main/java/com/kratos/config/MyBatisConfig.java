package com.kratos.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.kratos.interceptor.PageInteceptor;
import com.kratos.model.DruidBean;
import com.kratos.model.DruidFilterBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengping
 */
@Configuration
@EnableConfigurationProperties({DruidBean.class, DruidFilterBean.class})
@EnableTransactionManagement
public class MyBatisConfig{

    @Autowired
    DruidBean druidBean;
    @Autowired
    DruidFilterBean druidFilterBean;

    /*@Bean
    public ServletRegistrationBean druidServlet() {
        //设置监控登陆密码
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("loginUsername", druidFilterBean.getLoginUsername());
        initParameters.put("loginPassword", druidFilterBean.getLoginPassword());
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), druidFilterBean.getLoginUrl());
        bean.setInitParameters(initParameters);
        return bean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns(druidFilterBean.getUrlPatterns());
        filterRegistrationBean.addInitParameter("exclusions", druidFilterBean.getExclusions());
        return filterRegistrationBean;
    }*/

    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(druidBean.getDriverClassName());
        dataSource.setUrl(druidBean.getUrl());
        dataSource.setUsername(druidBean.getUsername());
        dataSource.setPassword(druidBean.getPassword());
        dataSource.setInitialSize(druidBean.getInitialSize());
        dataSource.setMinIdle(druidBean.getMinIdle());
        dataSource.setMaxActive(druidBean.getMaxActive());
        dataSource.setMaxWait(druidBean.getMaxWait());
        dataSource.setMinEvictableIdleTimeMillis(druidBean.getMinEvictableIdleTimeMillis());
        dataSource.setTimeBetweenEvictionRunsMillis(druidBean.getTimeBetweenEvictionRunsMillis());
        dataSource.setValidationQuery(druidBean.getValidationQuery());
        dataSource.setTestWhileIdle(druidBean.isTestWhileIdle());
        dataSource.setFilters(druidBean.getFilters());
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.kratos.model");
        //add  apge plugins
        bean.setPlugins(new Interceptor[]{new PageInteceptor()});
        //add xml
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
