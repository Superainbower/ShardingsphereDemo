package com.zkml.api.warning.dao;

import java.util.List;
import java.util.Map;

public interface WarningDao {
    List<Map> findRtaResult(Map map);
    Map getCountRtaResult(Map map);
    List<String> getTotalRtaResult(Map map);
    void deleteRtaAlarm(Map map);
    void setApplySN(Map map);
    List<Map> getRtaAlarm(Map map);
    List<Map> getWarningStatisticsByCar(Map map);
    List<Map> getWarningStatisticsByOrgan(Map map);
    List<String> getWarningStatisticsByCarPage(Map map);
    List<String> getWarningStatisticsByOrganPage(Map map);
    Map getWarningStatisticsByCarTotal(Map map);
    Map getWarningStatisticsByOrganTotal(Map map);
    int getWarningStatisticsNodeal(Map map);
    List<Map> getWarningStatisticsDetail(Map map);
    int getWarningStatisticsDetailPage(Map map);
}
