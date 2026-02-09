package com.easylive.utils;

import com.easylive.entity.config.AppConfig;
import com.easylive.entity.constants.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;

@Component
public class FFmpegUtils {
    @Resource
    private AppConfig appConfig;


    public void createImageThumbnail(String originFilePath) {
        String CMD = appConfig.getFfmpegPath() + " -i \"%s\" -vf scale=200:-1 \"%s\"";
        String fileName = StringTools.getFileName(originFilePath);
        CMD = String.format(CMD, originFilePath, fileName + Constants.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
    }

    public Integer getVideoInfoDuration(String completeVideo) {
        String CMD_GET_CODE = appConfig.getFfprobePath() + " -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 \"%s\"";
        String CMD = String.format(CMD_GET_CODE, completeVideo);
        String result = ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
        if (StringTools.isEmpty(result)) {
            return 0;
        }
        result = result.replace("\n", "");
        return new BigDecimal(result).intValue();
    }

    public String getVideoCodec(String videoFilePath) {
        String CMD_GET_CODE = appConfig.getFfprobePath() + " -v error -select_streams v:0 -show_entries stream=codec_name \"%s\"";
        String CMD = String.format(CMD_GET_CODE, videoFilePath);
        String result = ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
        result = result.replace("\n", "");
        result = result.substring(result.indexOf("=") + 1);
        String codec = result.substring(0, result.indexOf("["));
        return codec;
    }

    /**
     * 转码为h264
     */
    public void convertHevc2Mp4(String newFileName, String videoFilePath) {
        String CMD_HEVC_264 = appConfig.getFfmpegPath() + " -i \"%s\" -c:v libx264 -crf 20 \"%s\" -y";
        String cmd = String.format(CMD_HEVC_264, newFileName, videoFilePath);
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
    }

    public void convertVideo2Ts(File tsFolder, String videoFilePath) {
        String CMD_TRANSFER_2TS = appConfig.getFfmpegPath() + " -y -i \"%s\" -vcodec copy -acodec copy -bsf:v h264_mp4toannexb \"%s\"";
        String CMD_CUT_Ts = appConfig.getFfmpegPath() + " -i \"%s\" -c copy -map 0 -f segment -segment_list \"%s\" -segment_time 10 %s/%%4d.ts";
        String tsPath = tsFolder + "/" + Constants.TS_NAME;
        // 生成.ts
        String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
        // 生成索引文件.m3u8 和切片.ts
        cmd = String .format(CMD_CUT_Ts, tsPath, tsFolder.getPath() + "/" + Constants.M3U8_NAME, tsFolder.getPath());
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
        // 删除index.ts
        new File(tsPath).delete();
    }
}
