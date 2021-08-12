package com.zkml.api.warning.dao;

import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface TvmappingDao {
    List<Map> findAreaIdAndidcardByCarIds(List<String> carnos);
    List<String> findidcardByOrangList(List<String> organList);
    @MapKey("carid")
    Map<String,Map> findByOrganOrCarNos(Map map);

}
