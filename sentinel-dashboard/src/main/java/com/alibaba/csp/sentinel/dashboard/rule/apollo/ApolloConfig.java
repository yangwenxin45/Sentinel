package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.*;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConfigurationProperties(prefix = "apollo.portal")
@ComponentScan("com.alibaba.csp.sentinel.dashboard.rule.apollo")
@RefreshScope
public class ApolloConfig implements InitializingBean {

    public static String USERID = "apollo";
    public static String ENV = "DEV";
    public static String CLUSTERNAME = "default";
    public static String NAMESPACE = "application";
    public static String URL = "";

    private String url;
    private List<String> appNameConfigList = new ArrayList<>();
    private String userId;
    private String env = ENV;
    private String clusterName = CLUSTERNAME;
    private String namespace = NAMESPACE;

    // <appId, thirdId>
    public static volatile ConcurrentHashMap<String, String> thirdIdMap = new ConcurrentHashMap<>();
    // <applicationName, appId>
    public static volatile ConcurrentHashMap<String, String> appIdMap = new ConcurrentHashMap<>();
    // <applicationName, token>
    public static volatile ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public Converter<List<DegradeRuleEntity>, String> degradeRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    @Bean
    public Converter<List<AuthorityRuleEntity>, String> authorityRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<AuthorityRuleEntity>> authorityRuleEntityDecoder() {
        return s -> JSON.parseArray(s, AuthorityRuleEntity.class);
    }

    @Bean
    public Converter<List<SystemRuleEntity>, String> systemRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<SystemRuleEntity>> systemRuleEntityDecoder() {
        return s -> JSON.parseArray(s, SystemRuleEntity.class);
    }

    @Bean
    public Converter<List<ParamFlowRuleEntity>, String> paramFlowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<ParamFlowRuleEntity>> paramFlowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, ParamFlowRuleEntity.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ApolloConfig.ENV = env;
        ApolloConfig.USERID = userId;
        ApolloConfig.CLUSTERNAME = clusterName;
        ApolloConfig.NAMESPACE = namespace;
        ApolloConfig.URL = url;

        this.appNameConfigList.forEach(item -> {
            String[] items = item.split(":");
            if (items.length == 4) {
                String applicationName = items[0];
                String token = items[1];
                String appId = items[2];
                String thirdId = items[3];

                thirdIdMap.put(appId, thirdId);
                tokenMap.put(applicationName, token);
                appIdMap.put(applicationName, appId);
            }
        });
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getAppNameConfigList() {
        return appNameConfigList;
    }

    public void setAppNameConfigList(List<String> appNameConfigList) {
        this.appNameConfigList = appNameConfigList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return "ApolloConfig{" +
                "url='" + url + '\'' +
                ", appNameConfigList=" + appNameConfigList +
                ", userId='" + userId + '\'' +
                ", env='" + env + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
