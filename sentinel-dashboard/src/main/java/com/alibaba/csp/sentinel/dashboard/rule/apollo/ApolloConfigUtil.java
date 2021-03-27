package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

public final class ApolloConfigUtil {

    private static final String FLOW_RULE_TYPE = "flow";

    private static final String DEGRADE_RULE_TYPE = "degrade";

    private static final String PARAM_FLOW_RULE_TYPE = "param-flow";

    private static final String SYSTEM_RULE_TYPE = "system";

    private static final String AUTHORITY_RULE_TYPE = "authority";

    private static final String GATEWAY_FLOW_RULE_TYPE = "gateway-flow";

    private static final String API_DEFINITION_TYPE = "api-definition";

    // *-flow-rules
    private static final String FLOW_DATA_ID_POSTFIX = "-" + FLOW_RULE_TYPE + "-rules";

    // *-degrade-rules
    private static final String DEGRADE_DATA_ID_POSTFIX = "-" + DEGRADE_RULE_TYPE + "-rules";

    // *-param-flow-rules
    private static final String PARAM_FLOW_DATA_ID_POSTFIX = "-" + PARAM_FLOW_RULE_TYPE + "-rules";

    // *-system-rules
    private static final String SYSTEM_DATA_ID_POSTFIX = "-" + SYSTEM_RULE_TYPE + "-rules";

    // *-authority-rules
    private static final String AUTHORITY_DATA_ID_POSTFIX = "-" + AUTHORITY_RULE_TYPE + "-rules";

    // *-gateway-flow-rules
    private static final String GATEWAY_DATA_ID_POSTFIX = "-" + GATEWAY_FLOW_RULE_TYPE + "-rules";

    // *-api-definition-rules
    private static final String API_DEFINITION_DATA_ID_POSTFIX = "-" + API_DEFINITION_TYPE + "-rules";

    private static volatile ConcurrentHashMap<String, ApolloOpenApiClient> APOLLOOPENAPICLIENTMAP = new ConcurrentHashMap<>();

    public static String getFlowDataId(String appName) {
        return String.format("%s%s", appName, FLOW_DATA_ID_POSTFIX);
    }

    public static String getDegradeDataId(String appName) {
        return String.format("%s%s", appName, DEGRADE_DATA_ID_POSTFIX);
    }

    public static String getParamFlowDataId(String appName) {
        return String.format("%s%s", appName, PARAM_FLOW_DATA_ID_POSTFIX);
    }

    public static String getSystemDataId(String appName) {
        return String.format("%s%s", appName, SYSTEM_DATA_ID_POSTFIX);
    }

    public static String getAuthorityDataId(String appName) {
        return String.format("%s%s", appName, AUTHORITY_DATA_ID_POSTFIX);
    }

    public static String getGatewayFlowDataId(String appName) {
        return String.format("%s%s", appName, GATEWAY_DATA_ID_POSTFIX);
    }

    public static String getApiDefinitionDataId(String appName) {
        return String.format("%s%s", appName, API_DEFINITION_DATA_ID_POSTFIX);
    }

    public static ApolloOpenApiClient createApolloOpenApiClient(String appName) {
        ApolloOpenApiClient client = APOLLOOPENAPICLIENTMAP.get(appName);
        if (client != null) {
            return client;
        } else {
            String token = ApolloConfig.tokenMap.get(appName);
            if (StringUtils.isNotBlank(token)) {
                client = ApolloOpenApiClient.newBuilder()
                        .withPortalUrl(ApolloConfig.URL)
                        .withToken(token)
                        .build();
                APOLLOOPENAPICLIENTMAP.putIfAbsent(appName, client);
                return client;
            } else {
                System.err.println("根据指定的appName:" + appName + ",找不到对应的token");
                return null;
            }
        }
    }

    public static String getAppIdWithAppName(String appName) {
        return ApolloConfig.appIdMap.get(appName);
    }
}
