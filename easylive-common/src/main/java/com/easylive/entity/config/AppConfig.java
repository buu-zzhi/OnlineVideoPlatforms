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

    @Value("${es.index.video.name:easylive_video}")
    private String esIndexVideoName;

    public String getEsHostPort() {
        return esHostPort;
    }

    public String getEsIndexVideoName() {
        return esIndexVideoName;
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
