package com.zkml.api.warning.dao;

import com.zkml.api.warning.entity.AlarmTypeEntity;
import com.zkml.api.warning.entity.BaojingEntity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AlarmDao {
    int getCnt(Map map);
    List<BaojingEntity> getList(Map map);
    int getPositionCnt(Map map);
    List<BaojingEntity> getPositionList(Map map);
    void dealAlarm(Map map);
    void dealfenceAlarm(Map map);
    void setTerminalResult(Map map);
    List<Map> findTerminalAlarm(Map map);
    int getCountTerminalAlarm(Map map);
    List<Map> findTerminalResult(Map map);
    int getTermianlCount(Map map);
    int getFenceCount(Map map);

    @Select("select * from tb_alarm_type")
    @MapKey("type")
    Map<Integer,AlarmTypeEntity> findAllTypeMap();
}
