package com.kratos.service;


import com.kratos.model.App;

import java.util.List;

public interface AppService {
	App getAppByAppKey(String appKey);

	List<App> findAppByConditon(App app);

	App getById(Long id);
}
