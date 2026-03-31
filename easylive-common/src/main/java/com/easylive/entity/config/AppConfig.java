package com.easylive.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder:}")
    private String projectFolder;

    @Value("${admin.account:}")
    private String adminAccount;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${showFFmpegLog:false}")
    private Boolean showFFmpegLog;

    @Value("${ffmpeg.path:}")
    private String ffmpegPath;

    @Value("${ffmpeg.ffprobe:}")
    private String ffprobePath;

    @Value("${es.host.port:127.0.0.1:9200}")
    private String esHostPort;

    @Value("${es.username:}")
    private String esUsername;

    @Value("${es.password:}")
    private String esPassword;

    @Value("${es.ssl.enabled:false}")
    private Boolean esSslEnabled;

    @Value("${es.ssl.fingerprint:}")
    private String esSslFingerprint;

    @Value("${es.ssl.ca-cert-path:}")
    private String esSslCaCertPath;

    @Value("${es.ssl.insecure:false}")
    private Boolean esSslInsecure;

    @Value("${es.index.video.name:easylive_video}")
    private String esIndexVideoName;

    @Value("${es.index.video.analyzer:standard}")
    private String esIndexVideoAnalyzer;

    public String getEsHostPort() {
        return esHostPort;
    }

    public String getEsIndexVideoName() {
        return esIndexVideoName;
    }

    public String getEsIndexVideoAnalyzer() {
        return esIndexVideoAnalyzer;
    }

    public String getEsUsername() {
        return esUsername;
    }

    public String getEsPassword() {
        return esPassword;
    }

    public Boolean getEsSslEnabled() {
        return esSslEnabled;
    }

    public String getEsSslFingerprint() {
        return esSslFingerprint;
    }

    public String getEsSslCaCertPath() {
        return esSslCaCertPath;
    }

    public Boolean getEsSslInsecure() {
        return esSslInsecure;
    }

    public String getFfprobePath() {
        return ffprobePath;
    }

    public String getFfmpegPath() {
        return ffmpegPath;
    }

    public Boolean getShowFFmpegLog() {
        return showFFmpegLog;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getAdminAccount() {
        return adminAccount;
    }
}
