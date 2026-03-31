package com.easylive.entity.config;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeChatProperties;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionUtils;
import com.alibaba.cloud.ai.autoconfigure.dashscope.ResolvedConnectionProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnClass(DashScopeApi.class)
public class DashScopeApiConfiguration {

    @Bean
    @ConditionalOnMissingBean(DashScopeApi.class)
    public DashScopeApi dashScopeApi(DashScopeConnectionProperties connectionProperties,
                                     DashScopeChatProperties chatProperties,
                                     ObjectProvider<RestClient.Builder> restClientBuilderProvider,
                                     ObjectProvider<WebClient.Builder> webClientBuilderProvider,
                                     ObjectProvider<ResponseErrorHandler> responseErrorHandlerProvider) {
        ResolvedConnectionProperties resolved = DashScopeConnectionUtils.resolveConnectionProperties(
                connectionProperties,
                chatProperties,
                "chat"
        );

        return DashScopeApi.builder()
                .apiKey(resolved.apiKey())
                .headers(resolved.headers())
                .baseUrl(resolved.baseUrl())
                .workSpaceId(resolved.workspaceId())
                .restClientBuilder(restClientBuilderProvider.getIfAvailable(RestClient::builder))
                .webClientBuilder(webClientBuilderProvider.getIfAvailable(WebClient::builder))
                .responseErrorHandler(responseErrorHandlerProvider.getIfAvailable(DefaultResponseErrorHandler::new))
                .build();
    }
}
