package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("paramFlowRuleApolloPublisher")
public class ParamFlowRuleApolloPublisher implements DynamicRulePublisher<List<ParamFlowRuleEntity>> {

    private static FastDateFormat FASTDATEFORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    @Autowired
    private Converter<List<ParamFlowRuleEntity>, String> converter;

    @Override
    public void publish(String appName, List<ParamFlowRuleEntity> rules) throws Exception {
        if (rules == null) {
            return;
        }
        ApolloOpenApiClient client = ApolloConfigUtil.createApolloOpenApiClient(appName);
        if (client != null) {
            String dateFormat = FASTDATEFORMAT.format(new Date());

            // 具体的流控规则id
            String paramFlowDataId = ApolloConfigUtil.getParamFlowDataId(appName);
            // apollo的应用服务appId
            String appId = ApolloConfigUtil.getAppIdWithAppName(appName);

            // 1. 修改操作，预发布
            OpenItemDTO openItemDTO = new OpenItemDTO();
            openItemDTO.setKey(paramFlowDataId);
            openItemDTO.setValue(converter.convert(rules));
            openItemDTO.setComment("modify:" + dateFormat);
            openItemDTO.setDataChangeLastModifiedBy(ApolloConfig.USERID);
            openItemDTO.setDataChangeCreatedBy(ApolloConfig.USERID);
            client.createOrUpdateItem(appId, ApolloConfig.ENV, ApolloConfig.CLUSTERNAME, ApolloConfig.NAMESPACE, openItemDTO);

            // 2. 真正的进行发布
            NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
            namespaceReleaseDTO.setEmergencyPublish(true);
            namespaceReleaseDTO.setReleaseComment("publish:" + dateFormat);
            namespaceReleaseDTO.setReleaseTitle("发布新属性:" + dateFormat);
            namespaceReleaseDTO.setReleasedBy(ApolloConfig.USERID);
            client.publishNamespace(appId, ApolloConfig.ENV, ApolloConfig.CLUSTERNAME, ApolloConfig.NAMESPACE, namespaceReleaseDTO);
        } else {
            System.err.println("client is null, publish failed!");
        }
    }
}
