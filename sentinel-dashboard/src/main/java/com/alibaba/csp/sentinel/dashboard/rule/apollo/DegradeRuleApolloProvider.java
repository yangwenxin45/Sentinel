package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("degradeRuleApolloProvider")
public class DegradeRuleApolloProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

    @Autowired
    private Converter<String, List<DegradeRuleEntity>> converter;

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        ApolloOpenApiClient client = ApolloConfigUtil.createApolloOpenApiClient(appName);
        if (client != null) {
            // 具体的流控规则id
            String degradeDataId = ApolloConfigUtil.getDegradeDataId(appName);
            // apollo的应用服务appId
            String appId = ApolloConfigUtil.getAppIdWithAppName(appName);
            OpenNamespaceDTO dto = client.getNamespace(appId, ApolloConfig.ENV, ApolloConfig.CLUSTERNAME, ApolloConfig.NAMESPACE);
            String rules = dto.getItems().stream()
                    .filter(p -> p.getKey().equals(degradeDataId))
                    .map(OpenItemDTO::getValue)
                    .findFirst().orElse("");
            return converter.convert(rules);
        } else {
            return Collections.emptyList();
        }
    }

}
