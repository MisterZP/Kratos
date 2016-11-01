package com.kratos.service.impl;

import com.kratos.mapper.AppMapper;
import com.kratos.model.App;
import com.kratos.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppServiceImpl implements AppService {

	@Autowired private AppMapper appMapper;
	
	@Override
	public App getAppByAppKey(String appKey) {
		return appMapper.getAppByAppKey(appKey);
	}

	@Override
	public List<App> findAppByConditon(App app) {
		return appMapper.select(app);
	}

	@Override
	public App getById(Long id) {
		return appMapper.selectByPrimaryKey(id);
	}

}
