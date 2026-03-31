package com.easylive.web.controller;


import java.util.ArrayList;
import java.util.Date;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.easylive.web.annotation.GlobalInterceptor;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.po.VideoInfo;
import com.easylive.service.VideoDanmuService;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.entity.po.VideoDanmu;
import com.easylive.entity.query.VideoDanmuQuery;
import com.easylive.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @Description: 视频弹幕 Controller
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
@RestController
@RequestMapping("/danmu")
@Slf4j
public class VideoDanmuController extends ABaseController{

	@Resource
	private VideoDanmuService videoDanmuService;
    @Autowired
    private VideoInfoService videoInfoService;

    /*  发布弹幕  */
	@RequestMapping("/postDanmu")
    @GlobalInterceptor(checkLogin = true)
    @SentinelResource(value = "postDanmu", blockHandler = "postDanmuBlockHandler")
	public ResponseVO postDanmu(@NotEmpty String videoId, @NotEmpty String fileId,
                                @NotEmpty @Size(max=200) String text, @NotNull Integer mode,
                                @NotEmpty String color, @NotNull Integer time) {
        VideoDanmu videoDanmu = new VideoDanmu();
        videoDanmu.setVideoId(videoId);
        videoDanmu.setFileId(fileId);
        videoDanmu.setText(text);
        videoDanmu.setMode(mode);
        videoDanmu.setTime(time);
        videoDanmu.setColor(color);
        videoDanmu.setUserId(getTokenUserInfoDto().getUserId());
        videoDanmu.setPostTime(new Date());
        videoDanmuService.saveVideoDanmu(videoDanmu);
		return getSuccessResponseVO(null);
	}

    /**
     * postDanmu 限流降级处理方法
     */
    public ResponseVO postDanmuBlockHandler(String videoId, String fileId,
                                            String text, Integer mode,
                                            String color, Integer time,
                                            BlockException ex) {
        log.warn("发布弹幕接口被限流, videoId={}, fileId={}", videoId, fileId);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_ERROR);
        responseVO.setCode(ResponseCodeEnum.CODE_429.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_429.getMsg());
        return responseVO;
    }

    @RequestMapping("/loadDanmu")
    @SentinelResource(value = "loadDanmu", blockHandler = "loadDanmuBlockHandler")
    public ResponseVO loadDanmu(@NotEmpty String fileId, @NotEmpty String videoId) {
        VideoInfo videoInfo = videoInfoService.getVideoInfoByVideoId(videoId);
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ONE.toString())) {
            return getSuccessResponseVO(new ArrayList<>());
        }
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setFileId(fileId);
        videoDanmuQuery.setOrderBy("danmu_id asc");
        return getSuccessResponseVO(videoDanmuService.findListByParam(videoDanmuQuery));
    }

    /**
     * loadDanmu 限流降级处理方法
     */
    public ResponseVO loadDanmuBlockHandler(String fileId, String videoId, BlockException ex) {
        log.warn("加载弹幕接口被限流, fileId={}, videoId={}", fileId, videoId);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_ERROR);
        responseVO.setCode(ResponseCodeEnum.CODE_429.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_429.getMsg());
        return responseVO;
    }
}
