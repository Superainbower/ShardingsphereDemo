package com.zkml.api.warning.dao;

import java.util.List;
import java.util.Map;

public interface OverspeedDao {
    List<Map> getOverSpeedResult(Map map);
    int getOverSpeedTotalCnt(Map map);
    List<String> getOverSpeedPage(Map map);
    List<Map> getOverSpeedDetail(Map map);
    int getOverSpeedDetailPage(Map map);
    int getOverSpeedCount(Map map);
}
