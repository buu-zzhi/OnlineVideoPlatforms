package com.easylive.web.task;

import com.easylive.component.EsSearchComponent;
import com.easylive.component.RedisComponent;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.dto.VideoPlayInfoDto;
import com.easylive.entity.enums.SearchOrderTypeEnum;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.po.VideoInfoPost;
import com.easylive.service.VideoInfoPostService;
import com.easylive.service.VideoInfoService;
import com.easylive.service.VideoPlayHistoryService;
import com.easylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ExecuteQueueTask {
    private final ExecutorService executorService = Executors.newFixedThreadPool(Constants.length_2);

    private volatile boolean running = true;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private VideoInfoPostService videoInfoPostService;
    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private EsSearchComponent esSearchComponent;

    @Resource
    private VideoPlayHistoryService videoPlayHistoryService;

    // @PostConstruct会在Bean注入完成后自动执行
    @PostConstruct
    public void consumerTransferFileQueue() {
        executorService.execute(() ->{
            while(running && !Thread.currentThread().isInterrupted()) {
                try {
                    VideoInfoFilePost videoInfoFile = redisComponent.getFileFromTransferQueue();
                    if (videoInfoFile == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    videoInfoPostService.transferVideoFile(videoInfoFile);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("转码队列消费线程已停止");
                    break;
                } catch (Exception e) {
                    if (!running) {
                        log.info("应用正在关闭，停止消费转码队列");
                        break;
                    }
                    log.error("获取转码文件队列信息失败", e);
                }
            }
        });
    }

    @PostConstruct
    public void consumerVideoPlayQueue() {
        executorService.execute(() ->{
            while(running && !Thread.currentThread().isInterrupted()) {
                try {
                    VideoPlayInfoDto videoPlayInfoDto = redisComponent.getVideoPlayFromVideoPlayQueue();
                    if (videoPlayInfoDto == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    // 更新播放数量
                    videoInfoService.addReadCount(videoPlayInfoDto.getVideoId());

                    if (!StringTools.isEmpty(videoPlayInfoDto.getUserId())) {
                        // 记录历史播放
                        videoPlayHistoryService.saveHistory(videoPlayInfoDto.getUserId(), videoPlayInfoDto.getVideoId(), videoPlayInfoDto.getFileIndex());
                    }

                    // 按天记录视频播放
                    redisComponent.recordVideoPlayCount(videoPlayInfoDto.getVideoId());

                    // 更新es播放量
                    esSearchComponent.updateDocCount(videoPlayInfoDto.getVideoId(), SearchOrderTypeEnum.VIDEO_PLAY.getField(), 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("播放队列消费线程已停止");
                    break;
                } catch (Exception e) {
                    if (!running) {
                        log.info("应用正在关闭，停止消费播放队列");
                        break;
                    }
                    log.error("获取视频播放文件队列信息失败", e);
                }
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("队列消费线程池未能在规定时间内完全关闭");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("等待队列消费线程池关闭时被中断", e);
        }
    }
}
