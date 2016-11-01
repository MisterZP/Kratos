package com.kratos.mapper;

import com.kratos.base.BaseMapper;
import com.kratos.model.App;
import org.apache.ibatis.annotations.Select;


public interface AppMapper extends BaseMapper<App> {
	/**
	 * @param appKey
	 * @return App
	 */
	@Select("SELECT * FROM App WHERE appKey=#{appKey}")
	App getAppByAppKey(String appKey);
}
