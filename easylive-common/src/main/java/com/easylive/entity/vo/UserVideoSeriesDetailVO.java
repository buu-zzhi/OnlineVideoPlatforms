package com.easylive.entity.vo;

import com.easylive.entity.po.UserVideoSeries;
import com.easylive.entity.po.UserVideoSeriesVideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVideoSeriesDetailVO {
    private UserVideoSeries videoSeries;
    private List<UserVideoSeriesVideo> seriesVideoList;
}
