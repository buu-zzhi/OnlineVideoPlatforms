package com.easylive.web.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel 限流配置类
 * 仅针对弹幕接口进行限流控制
 * 同时负责将 dashboard 配置桥接到 Sentinel 运行时参数
 */
@Configuration
@Slf4j
public class SentinelConfig {

    @Value("${spring.application.name:easylive-web}")
    private String appName;

    @Value("${sentinel.dashboard.server:localhost:8858}")
    private String dashboardServer;

    @Value("${sentinel.api.port:8719}")
    private String apiPort;

    @Value("${sentinel.danmu.post-qps:100}")
    private int postDanmuQps;

    @Value("${sentinel.danmu.load-qps:500}")
    private int loadDanmuQps;

    /**
     * 注册 SentinelResourceAspect，使 @SentinelResource 注解生效
     */
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    /**
     * 初始化限流规则
     */
    @PostConstruct
    public void initFlowRules() {
        initDashboardTransport();

        List<FlowRule> rules = new ArrayList<>();

        // 发布弹幕接口限流
        FlowRule postDanmuRule = new FlowRule();
        postDanmuRule.setResource("postDanmu");
        postDanmuRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        postDanmuRule.setCount(postDanmuQps);
        postDanmuRule.setLimitApp("default");
        rules.add(postDanmuRule);

        // 加载弹幕接口限流
        FlowRule loadDanmuRule = new FlowRule();
        loadDanmuRule.setResource("loadDanmu");
        loadDanmuRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        loadDanmuRule.setCount(loadDanmuQps);
        loadDanmuRule.setLimitApp("default");
        rules.add(loadDanmuRule);

        FlowRuleManager.loadRules(rules);
        log.info("Sentinel 初始化完成: appName={}, dashboard={}, apiPort={}, postDanmu QPS={}, loadDanmu QPS={}",
                appName, dashboardServer, apiPort, postDanmuQps, loadDanmuQps);
    }

    /**
     * 将 Spring 配置桥接为 Sentinel dashboard 通信参数
     */
    private void initDashboardTransport() {
        System.setProperty("project.name", appName);
        System.setProperty("csp.sentinel.app.name", appName);
        System.setProperty("csp.sentinel.dashboard.server", dashboardServer);
        System.setProperty("csp.sentinel.api.port", apiPort);
    }
}
