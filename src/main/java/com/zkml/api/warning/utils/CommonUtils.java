package com.zkml.api.warning.utils;

public class CommonUtils {
    public static String getTable(String areaid){
        String table = "vbox_baojing_shard";
        if(areaid != null) {
            switch (areaid.substring(0, 7)) {
                case "0100102":
                    table = "vbox_baojing_shard";
                    break;
                case "0100120":
                    table = "vbox_baojing_qinghai_shard";
                    break;
                default:
                    table = "vbox_baojing_all_other_shard";
                    break;
            }
        }
        return table;
    }
}
