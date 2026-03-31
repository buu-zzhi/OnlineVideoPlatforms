package com.easylive.entity.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import jakarta.annotation.Resource;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Configuration
public class EsConfiguration extends ElasticsearchConfiguration {
    @Resource
    private AppConfig appConfig;

    @Override
    public ClientConfiguration clientConfiguration() {
        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder()
                .connectedTo(appConfig.getEsHostPort());

        ClientConfiguration.TerminalClientConfigurationBuilder terminalBuilder = builder;
        if (Boolean.TRUE.equals(appConfig.getEsSslEnabled())) {
            if (StringUtils.isNotBlank(appConfig.getEsSslFingerprint())) {
                terminalBuilder = builder.usingSsl(appConfig.getEsSslFingerprint());
            } else if (StringUtils.isNotBlank(appConfig.getEsSslCaCertPath())) {
                terminalBuilder = builder.usingSsl(buildSslContextFromCaCert(appConfig.getEsSslCaCertPath()));
            } else if (Boolean.TRUE.equals(appConfig.getEsSslInsecure()) || isLocalHost(appConfig.getEsHostPort())) {
                terminalBuilder = builder.usingSsl(buildInsecureSslContext(), NoopHostnameVerifier.INSTANCE);
            } else {
                terminalBuilder = builder.usingSsl();
            }
        }

        if (StringUtils.isNotBlank(appConfig.getEsUsername())) {
            terminalBuilder = terminalBuilder.withBasicAuth(appConfig.getEsUsername(), appConfig.getEsPassword());
        }

        return terminalBuilder.build();
    }

    private SSLContext buildInsecureSslContext() {
        try {
            return SSLContextBuilder.create()
                    .loadTrustMaterial((certificate, authType) -> true)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("构建ES SSL上下文失败", e);
        }
    }

    private SSLContext buildSslContextFromCaCert(String caCertPath) {
        try (InputStream inputStream = Files.newInputStream(Path.of(caCertPath))) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(inputStream);

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setCertificateEntry("es-ca", certificate);

            return SSLContextBuilder.create()
                    .loadTrustMaterial(trustStore, null)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("加载ES CA证书失败: " + caCertPath, e);
        }
    }

    private boolean isLocalHost(String hostPort) {
        if (StringUtils.isBlank(hostPort)) {
            return false;
        }
        String host = hostPort;
        int separatorIndex = hostPort.lastIndexOf(':');
        if (separatorIndex > 0) {
            host = hostPort.substring(0, separatorIndex);
        }
        return "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host) || "::1".equals(host);
    }
}
