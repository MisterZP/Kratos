package com.kratos.repository;

import com.kratos.model.App;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by zengping on 2016/12/7.
 */
public interface AppElasticRepository extends ElasticsearchRepository<App, Long> {

    @Query("{\"bool\" : {\"should\" : [{\"term\" : {\"appId\" : \"?0\"}}, {\"term\" : {\"appKey\" : \"?1\"}}]}}}")
    List<App> findByAppIdOrAppKey(Long appId, String appKey);
}
