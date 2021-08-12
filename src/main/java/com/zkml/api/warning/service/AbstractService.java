package com.zkml.api.warning.service;

import com.zkml.api.warning.entity.AlarmTypeEntity;
import com.zkml.api.warning.entity.BaojingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractService {

    /***
     *终端车辆映射信息表
     */
    List<Map> findAreaIdAndidcardByCarIds(List<String> carnos);
    List<String> findidcardByOrangList(List<String> organList);
    Map<String,Map> findByOrganOrCarNos(HashMap map);

    /***
     *围栏信息表
     */
    String findNameByfenceId(String fenceId);

    /***
     *终端报警表、电子围栏报警表
     */
    int getCnt(HashMap map);
    List<BaojingEntity> getList(HashMap map);
    int getPositionCnt(HashMap map);
    List<BaojingEntity> getPositionList(HashMap map);
    void dealAlarm(HashMap map);
    void dealfenceAlarm(HashMap map);
    Map<Integer,AlarmTypeEntity> findAllTypeMap();
    void setTerminalResult(HashMap map);
    List<Map> findTerminalAlarm(HashMap map);
    int getCountTerminalAlarm(HashMap map);
    List<Map> findTerminalResult(HashMap map);
    int getTermianlCount(HashMap map);
    int getFenceCount(HashMap map);

    /***
     *无任务报警表
     */
    Map getCountRtaResult(HashMap map);
    List<Map> findRtaResult(HashMap map);
    List<String> getTotalRtaResult(HashMap map);
    void deleteRtaAlarm(HashMap map);
    void setApplySN(HashMap map);
    List<Map> getRtaAlarm(Map map);
    List<Map> getWarningStatisticsByCar(Map map);
    List<Map> getWarningStatisticsByOrgan(Map map);
    List<String> getWarningStatisticsByCarPage(Map map);
    List<String> getWarningStatisticsByOrganPage(Map map);
    Map getWarningStatisticsByCarTotal(Map map);
    Map getWarningStatisticsByOrganTotal(Map map);
    int getWarningStatisticsNodeal(HashMap map);
    List<Map> getWarningStatisticsDetail(HashMap map);
    int getWarningStatisticsDetailPage(HashMap map);

    /***
     * 节假日报警表
     */
    void setHolidayApplySN(HashMap map);
    List<Map> getHolidayApplySN(HashMap map);

    /***
     * 申诉表
     */
    Map getAppealInfo(String id);

    /***
     * 超速报警表
     */
    List<Map> getOverSpeedResult(HashMap map);
    int getOverSpeedTotalCnt(HashMap map);
    List<String> getOverSpeedPage(HashMap map);
    List<Map> getOverSpeedDetail(HashMap map);
    int getOverSpeedDetailPage(HashMap map);
    int getOverSpeedCount(HashMap map);

    /***
     * 部署关系表
     */
    String getPsign(String deploySign);

    /***
     * 疲劳驾驶
     */
    int getDrivingCount(HashMap map);
}
