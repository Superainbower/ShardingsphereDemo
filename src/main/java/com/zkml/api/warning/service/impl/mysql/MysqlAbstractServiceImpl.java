package com.zkml.api.warning.service.impl.mysql;

import com.zkml.api.warning.dao.*;
import com.zkml.api.warning.entity.AlarmTypeEntity;
import com.zkml.api.warning.entity.BaojingEntity;
import com.zkml.api.warning.service.AbstractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MysqlAbstractServiceImpl implements AbstractService {

    @Resource
    AlarmDao alarmDao;
    @Resource
    FenceDao fenceDao;
    @Resource
    TvmappingDao tvmappingDao;
    @Resource
    WarningDao warningDao;
    @Resource
    HolidayDao holidayDao;
    @Resource
    AppealDao appealDao;
    @Resource
    OverspeedDao overspeedDao;
    @Resource
    DeploysignDao deploysignDao;
    @Resource
    FatigueDao fatigueDao;

    @Override
    public List<Map> findAreaIdAndidcardByCarIds(List<String> carnos){
        return tvmappingDao.findAreaIdAndidcardByCarIds(carnos);
    }

    @Override
    public List<String> findidcardByOrangList(List<String> organList){
        return tvmappingDao.findidcardByOrangList(organList);
    }

    @Override
    public Map<String,Map> findByOrganOrCarNos(HashMap map) {
        return tvmappingDao.findByOrganOrCarNos(map);
    }

    @Override
    public String findNameByfenceId(String fenceId){
        return fenceDao.findNameByfenceId(fenceId);
    }

    @Override
    public int getCnt(HashMap map) {
        return alarmDao.getCnt(map);
    }

    @Override
    public List<BaojingEntity> getList(HashMap map) {
        return alarmDao.getList(map);
    }

    @Override
    public int getPositionCnt(HashMap map) { return alarmDao.getPositionCnt(map); }

    @Override
    public List<BaojingEntity> getPositionList(HashMap map) { return alarmDao.getPositionList(map); }

    @Override
    public void dealAlarm(HashMap map) { alarmDao.dealAlarm(map); }

    @Override
    public void dealfenceAlarm(HashMap map) { alarmDao.dealfenceAlarm(map); }

    @Override
    public Map<Integer, AlarmTypeEntity> findAllTypeMap() {
        return alarmDao.findAllTypeMap();
    }

    @Override
    public void setTerminalResult(HashMap map) {
        alarmDao.setTerminalResult(map);
    }

    @Override
    public List<Map> findTerminalAlarm(HashMap map) {
        return alarmDao.findTerminalAlarm(map);
    }

    @Override
    public int getCountTerminalAlarm(HashMap map) {
        return alarmDao.getCountTerminalAlarm(map);
    }

    @Override
    public List<Map> findTerminalResult(HashMap map) {
        return alarmDao.findTerminalResult(map);
    }

    @Override
    public int getTermianlCount(HashMap map) {
        return alarmDao.getTermianlCount(map);
    }

    @Override
    public int getFenceCount(HashMap map) {
        return alarmDao.getFenceCount(map);
    }

    @Override
    public Map getCountRtaResult(HashMap map) {
        return warningDao.getCountRtaResult(map);
    }

    @Override
    public List<Map> findRtaResult(HashMap map) {
        return warningDao.findRtaResult(map);
    }

    @Override
    public List<String> getTotalRtaResult(HashMap map) { return warningDao.getTotalRtaResult(map); }

    @Override
    public void deleteRtaAlarm(HashMap map) {
        warningDao.deleteRtaAlarm(map);
    }

    @Override
    public void setApplySN(HashMap map) {
        warningDao.setApplySN(map);
    }

    @Override
    public List<Map> getRtaAlarm(Map map) {
        return warningDao.getRtaAlarm(map);
    }

    @Override
    public List<Map> getWarningStatisticsByCar(Map map) {
        return warningDao.getWarningStatisticsByCar(map);
    }

    @Override
    public List<Map> getWarningStatisticsByOrgan(Map map) {
        return warningDao.getWarningStatisticsByOrgan(map);
    }

    @Override
    public List<String> getWarningStatisticsByCarPage(Map map) {
        return warningDao.getWarningStatisticsByCarPage(map);
    }

    @Override
    public List<String> getWarningStatisticsByOrganPage(Map map) {
        return warningDao.getWarningStatisticsByOrganPage(map);
    }

    @Override
    public Map getWarningStatisticsByCarTotal(Map map) {
        return warningDao.getWarningStatisticsByCarTotal(map);
    }

    @Override
    public Map getWarningStatisticsByOrganTotal(Map map) {
        return warningDao.getWarningStatisticsByOrganTotal(map);
    }

    @Override
    public int getWarningStatisticsNodeal(HashMap map) {
        return warningDao.getWarningStatisticsNodeal(map);
    }

    @Override
    public List<Map> getWarningStatisticsDetail(HashMap map) {
        return warningDao.getWarningStatisticsDetail(map);
    }

    @Override
    public int getWarningStatisticsDetailPage(HashMap map) {
        return warningDao.getWarningStatisticsDetailPage(map);
    }

    @Override
    public void setHolidayApplySN(HashMap map) {
        holidayDao.setHolidayApplySN(map);
    }

    @Override
    public List<Map> getHolidayApplySN(HashMap map) {
        return holidayDao.getHolidayApplySN(map);
    }

    @Override
    public Map getAppealInfo(String id) {
        return appealDao.getAppealInfo(id);
    }

    @Override
    public List<Map> getOverSpeedResult(HashMap map) {
        return overspeedDao.getOverSpeedResult(map);
    }

    @Override
    public int getOverSpeedTotalCnt(HashMap map) {
        return overspeedDao.getOverSpeedTotalCnt(map);
    }

    @Override
    public List<String> getOverSpeedPage(HashMap map) {
        return overspeedDao.getOverSpeedPage(map);
    }

    @Override
    public List<Map> getOverSpeedDetail(HashMap map) {
        return overspeedDao.getOverSpeedDetail(map);
    }

    @Override
    public int getOverSpeedDetailPage(HashMap map) {
        return overspeedDao.getOverSpeedDetailPage(map);
    }

    @Override
    public int getOverSpeedCount(HashMap map) {
        return overspeedDao.getOverSpeedCount(map);
    }

    @Override
    public String getPsign(String deploySign) {
        return deploysignDao.getPsign(deploySign);
    }

    @Override
    public int getDrivingCount(HashMap map) {
        return fatigueDao.getDrivingCount(map);
    }


}
