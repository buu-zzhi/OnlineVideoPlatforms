package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.VideoDanmu;
import com.easylive.entity.query.VideoDanmuQuery;
/**
 * @Description: 视频弹幕 Service
 * @Author: false
 * @Date: 2026/02/10 21:56:04
 */
public interface VideoDanmuService{

	/**
 	 * 根据条件查询列表
 	 */
	List<VideoDanmu> findListByParam(VideoDanmuQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(VideoDanmuQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery query);

	/**
 	 * 新增
 	 */
	Integer add(VideoDanmu bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<VideoDanmu> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<VideoDanmu> listBean);

	/**
 	 * 根据 DanmuId 查询
 	 */
	VideoDanmu getVideoDanmuByDanmuId(Integer danmuId);

	/**
 	 * 根据 DanmuId 更新
 	 */
	Integer updateVideoDanmuByDanmuId(VideoDanmu bean, Integer danmuId); 

	/**
 	 * 根据 DanmuId 删除
 	 */
	Integer deleteVideoDanmuByDanmuId(Integer danmuId);

    void saveVideoDanmu(VideoDanmu videoDanmu);
}