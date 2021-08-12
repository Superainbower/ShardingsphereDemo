package com.zkml.api.warning.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.*;

public class YearShardingAlgorithm implements StandardShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        return null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<String> shardingValue) {
        Set<String> result = new LinkedHashSet<>();
        int lower = Integer.valueOf(shardingValue.getValueRange().lowerEndpoint().split("-")[0]);
        int upper = Integer.valueOf(shardingValue.getValueRange().upperEndpoint().split("-")[0]);
        Object[] tables = tableNames.toArray();
        String table = tables[0].toString().split("_20")[0];
        for(int i = lower; i <= upper; i ++){
            for (String each : tableNames) {
                if (each.endsWith(String.valueOf(i))) {
                    result.add(each);
                }
            }
        }
        List<Integer> yearList = new ArrayList<>();
        for(int i = 0; i < tables.length; i ++){
            if(tables[i].toString().contains("_20"))
                yearList.add( Integer.valueOf("20"+tables[i].toString().split("_20")[1]));
        }
        if(upper > Collections.max(yearList))
            result.add(table);
        return result;
    }

    @Override
    public void init() {
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }
}
