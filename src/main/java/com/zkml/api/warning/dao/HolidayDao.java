package com.zkml.api.warning.dao;

import java.util.List;
import java.util.Map;

public interface HolidayDao {
    void setHolidayApplySN(Map map);
    List<Map> getHolidayApplySN(Map map);
}
