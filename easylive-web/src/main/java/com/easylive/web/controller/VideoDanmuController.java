package com.easylive.web.controller;


import java.util.ArrayList;
import java.util.Date;

import com.easylive.web.annotation.GlobalInterceptor;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.po.VideoInfo;
import com.easylive.service.VideoDanmuService;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.entity.po.VideoDanmu;
import com.easylive.entity.query.VideoDanmuQuery;
import com.easylive.service.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Description: 视频弹幕 Controller
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
@RestController
@RequestMapping("/danmu")
public class VideoDanmuController extends ABaseController{

	@Resource
	private VideoDanmuService videoDanmuService;
    @Autowired
    private VideoInfoService videoInfoService;

    /*  发布弹幕  */
	@RequestMapping("/postDanmu")
    @GlobalInterceptor(checkLogin = true)
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

    @RequestMapping("/loadDanmu")
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


}