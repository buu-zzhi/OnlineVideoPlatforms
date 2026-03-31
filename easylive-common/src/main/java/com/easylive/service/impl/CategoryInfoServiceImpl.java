package com.easylive.service.impl;


import java.util.ArrayList;
import java.util.List;

import com.easylive.component.RedisComponent;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.VideoInfoQuery;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.CategoryInfoMapper;
import com.easylive.mapper.VideoInfoMapper;
import com.easylive.service.CategoryInfoService;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.CategoryInfo;
import com.easylive.entity.query.CategoryInfoQuery;
import com.easylive.service.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
/**
 * @Description: 分类信息 业务接口实现
 * @Author: false
 * @Date: 2026/02/01 19:48:05
 */
@Service("CategoryInfoMapper")
public class CategoryInfoServiceImpl implements CategoryInfoService{

	@Resource
	private CategoryInfoMapper<CategoryInfo, CategoryInfoQuery> categoryInfoMapper;

    @Resource
    private RedisComponent redisComponent;
    @Autowired
    private VideoInfoService videoInfoService;

    @Value("${app.cache.enabled:true}")
    private boolean cacheEnabled;

    /**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<CategoryInfo> findListByParam(CategoryInfoQuery query) {
        List<CategoryInfo> categoryInfoList = categoryInfoMapper.selectList(query);
        if (query.getConvert2Tree()!=null&&query.getConvert2Tree()) {
            categoryInfoList = convertLine2Tree(categoryInfoList, Constants.DEFAULT_PID);
        }
		return categoryInfoList;
    }

    private List<CategoryInfo> convertLine2Tree(List<CategoryInfo> dataList, Integer pid) {
        List<CategoryInfo> children = new ArrayList<>();
        for (CategoryInfo m: dataList) {
            if (m.getCategoryId() != null && m.getPCategoryId() != null && m.getPCategoryId().equals(pid)) {
                m.setChildren(convertLine2Tree(dataList, m.getCategoryId()));
                children.add(m);
            }
        }
        return children;
    }

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(CategoryInfoQuery query) {
		return this.categoryInfoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<CategoryInfo> findListByPage(CategoryInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<CategoryInfo> list = this.findListByParam(query);
		PaginationResultVO<CategoryInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(CategoryInfo bean) {
		return this.categoryInfoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<CategoryInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.categoryInfoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<CategoryInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.categoryInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 CategoryId 查询
 	 */
	@Override
	public CategoryInfo getCategoryInfoByCategoryId(Integer categoryId) {
		return this.categoryInfoMapper.selectByCategoryId(categoryId);}

	/**
 	 * 根据 CategoryId 更新
 	 */
	@Override
	public Integer updateCategoryInfoByCategoryId(CategoryInfo bean, Integer categoryId) {
		return this.categoryInfoMapper.updateByCategoryId(bean, categoryId);}

	/**
 	 * 根据 CategoryId 删除
 	 */
	@Override
	public Integer deleteCategoryInfoByCategoryId(Integer categoryId) {
		return this.categoryInfoMapper.deleteByCategoryId(categoryId);}

	/**
 	 * 根据 CategoryCode 查询
 	 */
	@Override
	public CategoryInfo getCategoryInfoByCategoryCode(String categoryCode) {
		return this.categoryInfoMapper.selectByCategoryCode(categoryCode);}

	/**
 	 * 根据 CategoryCode 更新
 	 */
	@Override
	public Integer updateCategoryInfoByCategoryCode(CategoryInfo bean, String categoryCode) {
		return this.categoryInfoMapper.updateByCategoryCode(bean, categoryCode);}

	/**
 	 * 根据 CategoryCode 删除
 	 */
	@Override
	public Integer deleteCategoryInfoByCategoryCode(String categoryCode) {
		return this.categoryInfoMapper.deleteByCategoryCode(categoryCode);}

    @Override
    public void saveCategory(CategoryInfo bean) {
        CategoryInfo dbBean = categoryInfoMapper.selectByCategoryCode(bean.getCategoryCode());
        if (bean.getCategoryId() == null && dbBean != null ||
            bean.getCategoryId() != null && dbBean != null && !dbBean.getCategoryId().equals(bean.getCategoryId())) {
            throw new BusinessException("分类编号已经存在");
        }
        if (bean.getCategoryId() == null) {
            Integer maxSort = categoryInfoMapper.selectMaxSort(bean.getPCategoryId());
            bean.setSort(maxSort + 1);
            categoryInfoMapper.insert(bean);
        } else {
            categoryInfoMapper.updateByCategoryId(bean, bean.getCategoryId());
        }
        save2Redis();
    }

    @Override
    public void delCategory(Integer categoryId) {
        // 查询分类下是否有视频
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryOrPCategoryId(categoryId);
        Integer count = videoInfoService.findCountByParam(videoInfoQuery);
        if (count > 0) {
            throw new BusinessException("分类下有视频信息，不允许删除");
        }

        CategoryInfoQuery categoryInfoQuery = new CategoryInfoQuery();
        categoryInfoQuery.setCategoryIdOrPCategoryId(categoryId);
        categoryInfoMapper.deleteByParam(categoryInfoQuery);
        save2Redis();
    }

    @Override
    public void changeSort(Integer pCategoryId, String categoryIds) {
        String[] categoryIdArray = categoryIds.split(",");
        List<CategoryInfo> categoryInfoList = new ArrayList<>();
        Integer sort = 1;
        for (String categoryId : categoryIdArray) {
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setCategoryId(Integer.parseInt(categoryId));
            categoryInfo.setPCategoryId(pCategoryId);
            categoryInfo.setSort(++sort);
            categoryInfoList.add(categoryInfo);
        }
        categoryInfoMapper.updateSortBatch(categoryInfoList);
        save2Redis();
    }

    private void save2Redis() {
        CategoryInfoQuery query = new CategoryInfoQuery();
        query.setOrderBy("sort asc");
        query.setConvert2Tree(true);
        List<CategoryInfo> categoryInfoList = findListByParam(query);
        redisComponent.saveCategoryList(categoryInfoList);
    }

    @Override
    public List<CategoryInfo> getAllCategoryList() {
        // 如果关闭缓存，直接查数据库（用于性能对比测试）
        if (!cacheEnabled) {
            CategoryInfoQuery query = new CategoryInfoQuery();
            query.setOrderBy("sort asc");
            query.setConvert2Tree(true);
            return findListByParam(query);
        }
        // 走 Redis 缓存
        List<CategoryInfo> categoryInfoList = redisComponent.getCategoryList();
        if (categoryInfoList.isEmpty()) {
            save2Redis();
        }
        return redisComponent.getCategoryList();
    }
}