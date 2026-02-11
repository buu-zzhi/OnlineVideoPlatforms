package com.easylive.entity.vo;

import com.easylive.entity.po.VideoInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoInfoResultVO {
    private VideoInfo videoInfo;
    private List userActionList;


}
