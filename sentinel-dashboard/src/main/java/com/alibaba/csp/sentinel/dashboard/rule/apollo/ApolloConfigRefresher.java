package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApolloConfigRefresher {

    private static final Logger logger = LoggerFactory.getLogger(ApolloConfigRefresher.class);

    @Resource
    private ApolloConfig apolloConfig;

    @Resource
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener(value = {ConfigConsts.NAMESPACE_APPLICATION}, interestedKeyPrefixes = "apollo.portal.")
    public void onChange(ConfigChangeEvent changeEvent) {
        logger.info("before refresh {}", apolloConfig.toString());
        refreshScope.refresh("apolloConfig");
        logger.info("after refresh {}", apolloConfig.toString());
    }

}
