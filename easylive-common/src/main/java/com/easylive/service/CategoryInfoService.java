package com.easylive.service;


import java.util.List;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.po.CategoryInfo;
import com.easylive.entity.query.CategoryInfoQuery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @Description: 分类信息 Service
 * @Author: false
 * @Date: 2026/02/01 19:48:05
 */
public interface CategoryInfoService{

	/**
 	 * 根据条件查询列表
 	 */
	List<CategoryInfo> findListByParam(CategoryInfoQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(CategoryInfoQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<CategoryInfo> findListByPage(CategoryInfoQuery query);

	/**
 	 * 新增
 	 */
	Integer add(CategoryInfo bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<CategoryInfo> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<CategoryInfo> listBean);

	/**
 	 * 根据 CategoryId 查询
 	 */
	CategoryInfo getCategoryInfoByCategoryId(Integer categoryId);

	/**
 	 * 根据 CategoryId 更新
 	 */
	Integer updateCategoryInfoByCategoryId(CategoryInfo bean, Integer categoryId); 

	/**
 	 * 根据 CategoryId 删除
 	 */
	Integer deleteCategoryInfoByCategoryId(Integer categoryId);

	/**
 	 * 根据 CategoryCode 查询
 	 */
	CategoryInfo getCategoryInfoByCategoryCode(String categoryCode);

	/**
 	 * 根据 CategoryCode 更新
 	 */
	Integer updateCategoryInfoByCategoryCode(CategoryInfo bean, String categoryCode); 

	/**
 	 * 根据 CategoryCode 删除
 	 */
	Integer deleteCategoryInfoByCategoryCode(String categoryCode);

    void saveCategory(CategoryInfo categoryInfo);

    void delCategory(Integer categoryId);

    void changeSort(@NotNull Integer pCategoryId, @NotEmpty String categoryIds);

    public List<CategoryInfo> getAllCategoryList();
}