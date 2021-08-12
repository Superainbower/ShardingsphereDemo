package com.zkml.api.warning.controller;

import com.alibaba.fastjson.JSONArray;
import com.zkml.api.warning.entity.AlarmTypeEntity;
import com.zkml.api.warning.entity.BaojingEntity;
import com.zkml.api.warning.service.ServiceFactory;
import com.zkml.api.warning.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("alarm")
public class AlarmController {
    private static final Logger logger = LoggerFactory.getLogger(AlarmController.class);

    @Resource
    ServiceFactory serviceFactory;

    @GetMapping("queryRtaResult")
    public HashMap queryRtaResult(HttpServletRequest request, HttpServletResponse response){
        HashMap queryMap=new HashMap<>();
        HashMap<String,Object> alarmResult=new HashMap<>();
        //设置分页查询条件
        Integer page=(request.getParameter("page")==null) ? 0 : Integer.parseInt(request.getParameter("page"));
        Integer count=(request.getParameter("count")==null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String export = request.getParameter("export")==null ? "0" : request.getParameter("export");
        String status = request.getParameter("status")==null ? "0" : request.getParameter("status");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String organno = request.getParameter("organno");
        logger.info(request.getRemoteAddr() +" 访问：alarm/queryRtaResult,startTime:{},endTime:{},organno:{}",startTime,endTime,organno);
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("startTime",startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("organno", organno);
        queryMap.put("export",export);
        queryMap.put("status",status);
        if(request.getParameter("type") != null){
            queryMap.put("type", Arrays.asList(request.getParameter("type").split(",")));
        }
        if(request.getParameter("carno")!=null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        if(request.getParameter("idcard")!=null){
            queryMap.put("idcard", Arrays.asList(request.getParameter("idcard").split(",")));
        }
        if(request.getParameter("warnseconds")!=null){
            queryMap.put("warnseconds",request.getParameter("warnseconds"));
        }
        if(request.getParameter("miles")!=null){
            queryMap.put("miles",request.getParameter("miles"));
        }
        Map cntMap = serviceFactory
                .getAbstractService()
                .getCountRtaResult(queryMap);
        queryMap.put("export","1");
        Map totalcntMap = serviceFactory
                .getAbstractService()
                .getCountRtaResult(queryMap);
        int totalCnt = serviceFactory
                .getAbstractService()
                .getTotalRtaResult(queryMap).size();
        List<Map> result = serviceFactory
                .getAbstractService()
                .findRtaResult(queryMap);
        alarmResult.put("retCode",0);
        alarmResult.put("des", "成功");
        alarmResult.put("result", result);
        alarmResult.put("totalCnt",totalCnt);
        alarmResult.put("totalType1Cnt",totalcntMap.get("type1cnt"));
        alarmResult.put("totalType2Cnt",totalcntMap.get("type2cnt"));
        alarmResult.put("type1Cnt",cntMap.get("type1cnt"));
        alarmResult.put("type2Cnt",cntMap.get("type2cnt"));
        return alarmResult;
    }

    @GetMapping("deleteRtaAlarm")
    public HashMap deleteRtaAlarm(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        String[] id = request.getParameter("id").split(",");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        logger.info(request.getRemoteAddr() +" 访问：alarm/deleteRtaAlarm,startTime:{},endTime:{}",startTime,endTime);
        HashMap parmap = new HashMap();
        parmap.put("id", Arrays.asList(id));
        parmap.put("startTime",startTime);
        parmap.put("endTime", endTime);
        serviceFactory
                .getAbstractService()
                .deleteRtaAlarm(parmap);
        resultMap.put("retCode", 0);
        resultMap.put("des",  "成功");
        return resultMap;
    }

    @GetMapping("setApplySN")
    public HashMap setApplySN(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        String applySN = request.getParameter("applySN");
        String orderId = request.getParameter("orderId");
        String deploySign = request.getParameter("deploySign");
        String applyList = request.getParameter("applyList");
        String appealStatus = request.getParameter("appealStatus")==null ? "5" : request.getParameter("appealStatus");
        logger.info(request.getRemoteAddr() +" 访问：alarm/setApplySN,applyList:{},appealStatus:{}",applyList,appealStatus);
        HashMap parmap = new HashMap();
        parmap.put("applySN", applySN);
        parmap.put("orderId", orderId);
        parmap.put("deploySign", deploySign);
        parmap.put("appealStatus",appealStatus);
        List<Map<String,String>> list = JSONArray.parseObject(applyList,List.class);
        for(int i = 0; i < list.size(); i ++){
            parmap.put("carnoList", Arrays.asList(list.get(i).get("carno")));
            parmap.put("startTime", list.get(i).get("startTime"));
            parmap.put("endTime", list.get(i).get("endTime"));
            serviceFactory
                    .getAbstractService()
                    .setApplySN(parmap);
            if(appealStatus.equals("5")){
                dealHolidayApply(parmap);
            }
        }
        resultMap.put("retCode", 0);
        resultMap.put("des",  "成功");
        return resultMap;
    }

    public void dealHolidayApply(HashMap map){
        List<Map> list = serviceFactory
                .getAbstractService()
                .getHolidayApplySN(map);
        if(list.size() != 0 ){
            for(int i = 0 ; i < list.size(); i ++){
                String id = String.valueOf(list.get(i).get("id"));
                String apply_sn_str = "";
                String apply_status = (map.get("appealStatus").toString().equals("5")) ? "2" : "3";
                if(list.get(i).get("apply_sn_str") != null){
                    apply_sn_str = String.valueOf(list.get(i).get("apply_sn_str"));
                    apply_sn_str =  apply_sn_str + ","+ map.get("applySN")+"|"+map.get("orderId")+"|"+map.get("deploySign");
                }else{
                    apply_sn_str =  map.get("applySN")+"|"+map.get("orderId")+"|"+map.get("deploySign");
                }
                HashMap papra = new HashMap();
                papra.put("id",id);
                papra.put("apply_sn_str",apply_sn_str);
                papra.put("apply_status",apply_status);
                papra.put("startTime",map.get("startTime"));
                papra.put("endTime",map.get("endTime"));
                serviceFactory
                        .getAbstractService()
                        .setHolidayApplySN(papra);
            }
        }
    }

    @GetMapping("queryRtaAlarm")
    public HashMap queryRtaAlarm(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap=new HashMap<>();
        HashMap result=new HashMap();
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String organno = request.getParameter("organno");
        String[] carnos = request.getParameter("carno").split(",");
        logger.info(request.getRemoteAddr() +" 访问：alarm/queryRtaAlarm,organno={},startTime={},endTime={}",organno,startTime,endTime);
        queryMap.put("startTime",startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("organno", organno);
        queryMap.put("carno", Arrays.asList(carnos));
        if(request.getParameter("type") != null){
            queryMap.put("type", Arrays.asList(request.getParameter("type").split(",")));
        }
        if(request.getParameter("warnseconds")!=null){
            queryMap.put("warnseconds",request.getParameter("warnseconds"));
        }
        if(request.getParameter("miles")!=null){
            queryMap.put("miles",request.getParameter("miles"));
        }
        List<Map> sqlResult = serviceFactory
                .getAbstractService()
                .getRtaAlarm(queryMap);
        HashMap<String,Integer> tmpMap = new HashMap<>();
        for(int i = 0 ; i < sqlResult.size(); i ++){
            tmpMap.put(sqlResult.get(i).get("car_no").toString(),Integer.parseInt(sqlResult.get(i).get("cnt").toString()));
        }
        int cnts = 0;
        for(int i = 0 ; i < carnos.length; i++){
            if(tmpMap.get(carnos[i]) == null){
                result.put(carnos[i],0);
            }else{
                result.put(carnos[i],tmpMap.get(carnos[i]));
                cnts = cnts + tmpMap.get(carnos[i]);
            }
        }
        resultMap.put("result",result);
        resultMap.put("cnt",cnts);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        return resultMap;
    }

    @GetMapping("getWarningStatistics")
    public HashMap getWarningStatistics(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap = new HashMap();
        Map cntMap = new HashMap();
        List<Map> resList = new ArrayList<>();
        int pageCnt = 0;
        int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
        int count = (request.getParameter("count") == null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String export = request.getParameter("export") == null ? "0" : request.getParameter("export");
        String startDay = request.getParameter("startday")+" 00:00:00";
        String endDay = request.getParameter("endday")+" 23:59:59";
        queryMap.put("type", Arrays.asList(request.getParameter("type").split(",")));
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("export",export);
        queryMap.put("startday", startDay);
        queryMap.put("endday",endDay);
        String source = request.getParameter("source");
        logger.info(request.getRemoteAddr() +" 访问：alarm/getWarningStatistics,startday={},endday={},source={}",startDay,endDay,source);
        //业务用
        if(source.equals("1")){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
            queryMap.put("organno", request.getParameter("organno"));
            resList = serviceFactory
                    .getAbstractService()
                    .getWarningStatisticsByCar(queryMap);

            pageCnt = serviceFactory
                    .getAbstractService()
                    .getWarningStatisticsByCarPage(queryMap).size();

            cntMap = serviceFactory
                    .getAbstractService()
                    .getWarningStatisticsByCarTotal(queryMap);
        }
        //监督用
        else{
            queryMap.put("organno", Arrays.asList(request.getParameter("organno").split(",")));
            if(request.getParameter("carno") != null){
                queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
            }
            resList = serviceFactory
                    .getAbstractService()
                    .getWarningStatisticsByOrgan(queryMap);

            pageCnt = serviceFactory
                    .getAbstractService()
                    .getWarningStatisticsByOrganPage(queryMap).size();

            cntMap = serviceFactory
                    .getAbstractService()
                    .getWarningStatisticsByOrganTotal(queryMap);
        }
        resultMap.put("listMap",resList);
        resultMap.put("cntMap",cntMap);
        resultMap.put("pageCnt",pageCnt);
        resultMap.put("retCode", 0);
        resultMap.put("des","成功");
        return resultMap;
    }

    @GetMapping("getWarningStatisticsCnt")
    public HashMap getWarningStatisticsCnt(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap = new HashMap();
        String startDay = request.getParameter("startday")+" 00:00:00";
        String endDay = request.getParameter("endday")+" 23:59:59";
        queryMap.put("type", Arrays.asList(request.getParameter("type").split(",")));
        queryMap.put("startday", startDay);
        queryMap.put("endday",endDay);
        String source = request.getParameter("source");
        logger.info(request.getRemoteAddr() +" 访问：alarm/getWarningStatisticsCnt,startday={},endday={},source={}",startDay,endDay,source);
        if(request.getParameter("carno") != null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        if(request.getParameter("organno") != null){
            queryMap.put("organno", Arrays.asList(request.getParameter("organno").split(",")));
        }
        int total = serviceFactory
                .getAbstractService()
                .getWarningStatisticsNodeal(queryMap);
        resultMap.put("total",total);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        return  resultMap;
    }

    @GetMapping("getWarningStatisticsDetail")
    public HashMap getWarningStatisticsDetail(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap = new HashMap();
        List<Map> unionList = new ArrayList<>();
        int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
        int count = (request.getParameter("count") == null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String export = request.getParameter("export") == null ? "0" : request.getParameter("export");
        String startDay = request.getParameter("startday")+" 00:00:00";
        String endDay = request.getParameter("endday")+" 23:59:59";
        String status = request.getParameter("status");
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("export",export);
        queryMap.put("startday", startDay);
        queryMap.put("endday",endDay);
        queryMap.put("type", Arrays.asList(request.getParameter("type").split(",")));
        queryMap.put("status", status);
        logger.info(request.getRemoteAddr() +" 访问：alarm/getWarningStatisticsDetail,startday={},endday={},status={}",startDay,endDay,status);

        if(request.getParameter("appealstatus")!=null){
            queryMap.put("appealstatus", Arrays.asList(request.getParameter("appealstatus").split(",")));
        }
        if(request.getParameter("applysn")!=null){
            queryMap.put("applysn", request.getParameter("applysn"));
        }
        if(request.getParameter("miles")!=null){
            queryMap.put("miles", request.getParameter("miles"));
        }
        if(request.getParameter("warnseconds")!=null){
            queryMap.put("warnseconds", request.getParameter("warnseconds"));
        }
        if(request.getParameter("carno")!=null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        if(request.getParameter("msgstatus")!=null){
            queryMap.put("msgstatus", request.getParameter("msgstatus"));
        }
        if(request.getParameter("organno")!=null){
            queryMap.put("organno", Arrays.asList(request.getParameter("organno").split(",")));
        }

        List<Map> resList = serviceFactory
                .getAbstractService()
                .getWarningStatisticsDetail(queryMap);
        for(Map resMap: resList){
           Map applyMap = serviceFactory
                    .getAbstractService()
                    .getAppealInfo(resMap.get("id").toString());
           if(applyMap != null) {
               resMap.put("appeal_content", applyMap.get("appeal_content"));
               resMap.put("appeal_realname", applyMap.get("appeal_realname"));
               resMap.put("appeal_phone", applyMap.get("appeal_phone"));
               resMap.put("date_created", applyMap.get("date_created"));
           }
            unionList.add(resMap);
        }

        int pageCnt =  serviceFactory
                .getAbstractService()
                .getWarningStatisticsDetailPage(queryMap);
        resultMap.put("listMap",unionList);
        resultMap.put("pageCnt",pageCnt);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        return  resultMap;
    }

    @GetMapping("queryTerminalAlarm")
    public HashMap queryTerminalAlarm(HttpServletRequest request, HttpServletResponse response){
        HashMap alarmResult=new HashMap<>();
        HashMap queryMap=new HashMap<>();
        List<Map> unionList = new ArrayList<>();
        int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
        int count = (request.getParameter("count") == null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String alarmType = request.getParameter("alarmType");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String areaId = request.getParameter("areaId");
        String queryType = request.getParameter("queryType");

        logger.info(request.getRemoteAddr() +" 访问：alarm/queryTerminalAlarm,startTime={},endTime={},areaId={}",startTime,endTime,areaId);

        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("startTime",startTime);
        queryMap.put("endTime", endTime);
        if(request.getParameter("carno")!=null){
            queryMap.put("carnoList", Arrays.asList(request.getParameter("carno").split("\\|")));
        }
        if(request.getParameter("organ")!=null){
            queryMap.put("organList", Arrays.asList(request.getParameter("organ").split("\\|")));
        }
        queryMap.put("table", CommonUtils.getTable(areaId));
        queryMap.put("typeList",Arrays.asList("2".split(",")));
        //获取报警类型
        Map<Integer,AlarmTypeEntity> alarmTypeEntityMap =  serviceFactory
                .getAbstractService()
                .findAllTypeMap();
        Map<String,String> alarmContentMap=new HashMap<>();
        alarmTypeEntityMap.forEach((k, v) -> {
            for(String content: v.getContext().split("\\|")){
                alarmContentMap.put(content,v.getDescription());
            }
        });
        if(queryType != null) {
            String[] types = queryType.split(",");
            String typeContext = "";
            for (int i = 0; i < types.length; i++) {
                AlarmTypeEntity alarmTypeEntity = alarmTypeEntityMap.get(Integer.valueOf(types[i]));
                typeContext = typeContext + alarmTypeEntity.getContext() + "|";
            }
            queryMap.put("alarmType", Arrays.asList(typeContext.substring(0, typeContext.length() - 1).split("\\|")));
        }else {
            AlarmTypeEntity alarmTypeEntity = alarmTypeEntityMap.get(alarmType);
            if (alarmTypeEntity != null){
                queryMap.put("alarmType",Arrays.asList(alarmTypeEntity.getContext().split("\\|")));
            } else {
                queryMap.put("alarmType",alarmContentMap.keySet());
            }
        }

        Map<String,Map> tvmappingMap = serviceFactory
                .getAbstractService()
                .findByOrganOrCarNos(queryMap);

        List<BaojingEntity> resList = serviceFactory
                .getAbstractService()
                .getList(queryMap);

        int totalCnt = serviceFactory
                .getAbstractService()
                .getCnt(queryMap);

        for(BaojingEntity baojingEntity:resList){
            HashMap resMap = new HashMap();
            resMap.put("carno",baojingEntity.getCarno());
            resMap.put("alarmTime",baojingEntity.getDaq_time());
            resMap.put("address",baojingEntity.getAddress());
            resMap.put("content",alarmContentMap.get(baojingEntity.getContent()));
            resMap.put("terminalId",tvmappingMap.get(baojingEntity.getCarno()) == null? null:tvmappingMap.get(baojingEntity.getCarno()).get("terminalid"));
            unionList.add(resMap);
        }

        alarmResult.put("retCode", 0);
        alarmResult.put("des", "成功");
        alarmResult.put("result", unionList);
        alarmResult.put("totalCnt",totalCnt);
        return alarmResult;
    }

    @GetMapping("setTerminalResult")
    public HashMap setTerminalResult(HttpServletRequest request, HttpServletResponse response){
        HashMap result = new HashMap();
        HashMap queryMap=new HashMap<>();
        queryMap.put("id",request.getParameter("id"));
        queryMap.put("daqtime",request.getParameter("daqtime"));
        queryMap.put("table", CommonUtils.getTable(request.getParameter("areaid")));
        serviceFactory
                .getAbstractService()
                .setTerminalResult(queryMap);
        result.put("retCode",0);
        result.put("des","成功");
        return result;
    }

    @GetMapping("getTerminalResult")
    public HashMap getTerminalResult(HttpServletRequest request, HttpServletResponse response){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        String endDay=dateFormat.format(calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY,-144);
        String startDay=dateFormat.format(calendar.getTime());
        HashMap queryMap=new HashMap<>();
        HashMap<String,Object> alarmResult=new HashMap<>();
        //设置分页查询条件
        int page=(request.getParameter("page")==null) ? 0 : Integer.parseInt(request.getParameter("page"));
        int count=(request.getParameter("count")==null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String organno = request.getParameter("organno");
        logger.info(request.getRemoteAddr() +" 访问：alarm/getTerminalResult,organno={}",organno);
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("organno", organno);
        queryMap.put("startTime",startDay+" 00:00:00");
        queryMap.put("endTime",endDay+" 23:59:59");
        if(request.getParameter("carno")!=null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        if(request.getParameter("status")!=null){
            queryMap.put("status", request.getParameter("status"));
        }
        if(request.getParameter("content")!=null){
            queryMap.put("content", Arrays.asList(request.getParameter("content").split(",")));
        }
        String table = "vbox_baojing";
        if(request.getParameter("areaid")!=null) {
            switch (request.getParameter("areaid").substring(0, 7)) {
                case "0100102":
                    table = "vbox_baojing";
                    break;
                case "0100120":
                    table = "vbox_baojing_qinghai";
                    break;
                default:
                    table = "vbox_baojing_all_other";
                    break;
            }
        }
        queryMap.put("table", table);

        int totalCnt = serviceFactory
                .getAbstractService()
                .getCountTerminalAlarm(queryMap);
        List<Map> result = serviceFactory
                .getAbstractService()
                .findTerminalAlarm(queryMap);
        alarmResult.put("retCode",0);
        alarmResult.put("des","成功");
        alarmResult.put("result", result);
        alarmResult.put("totalCnt",totalCnt);
        return alarmResult;
    }

    @GetMapping("queryTerminalResult")
    public HashMap queryTerminalResult(HttpServletRequest request, HttpServletResponse response){
        HashMap queryMap=new HashMap<>();
        HashMap<String,Object> alarmResult=new HashMap<>();
        List<Map> unionList = new ArrayList<>();
        //设置分页查询条件
        Integer page=(request.getParameter("page")==null) ? 0 : Integer.parseInt(request.getParameter("page"));
        Integer count=(request.getParameter("count")==null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String organno = request.getParameter("organno");
        logger.info(request.getRemoteAddr() +" 访问：alarm/queryTerminalResult,organno={},startTime={},endTime={}",organno,startTime,endTime);
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("startTime",startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("organno", organno);
        queryMap.put("typeList", Arrays.asList(request.getParameter("type").split(",")));
        if(request.getParameter("carno")!=null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        queryMap.put("table", CommonUtils.getTable(request.getParameter("areaid")));

        queryMap.put("export","0");
        List<Map> resList = serviceFactory
                .getAbstractService()
                .findTerminalResult(queryMap);

        Map<String,Map> tvmappingMap = serviceFactory
                .getAbstractService()
                .findByOrganOrCarNos(queryMap);

        for(Map map:resList){
            HashMap resMap = new HashMap();
            resMap.put("carno",map.get("carno"));
            resMap.put("cnt",map.get("cnt"));
            resMap.put("content",map.get("con").equals("1")?"疲劳驾驶报警":"GPS模块故障报警");
            resMap.put("terminalId",tvmappingMap.get(map.get("carno")) == null? null:tvmappingMap.get(map.get("carno")).get("terminalid"));
            unionList.add(resMap);
        }

        queryMap.put("export","1");
        int totalCnt = serviceFactory
                .getAbstractService()
                .findTerminalResult(queryMap).size();

        alarmResult.put("retCode", 0);
        alarmResult.put("des", "成功");
        alarmResult.put("result", unionList);
        alarmResult.put("totalCnt",totalCnt);
        return alarmResult;
    }

    @GetMapping("getOverSpeedResult")
    public HashMap getOverSpeedResult(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap=new HashMap<>();
        Integer page=(request.getParameter("page")==null) ? 0 : Integer.parseInt(request.getParameter("page"));
        Integer count=(request.getParameter("count")==null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String export = request.getParameter("export")==null ? "0" : request.getParameter("export");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String organno = request.getParameter("organno");
        String deploySign = request.getParameter("deploySign")+"_API";
        logger.info(request.getRemoteAddr() +" 访问：alarm/getOverSpeedResult,deploySign={},organno={},startTime={},endTime={}",deploySign,organno,startTime,endTime);
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("export",export);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("organno", organno);
        if(request.getParameter("carno") != null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        int cnt = 0;
        int totalCnt = 0;
        int total = 0;
        String psign = serviceFactory
                    .getAbstractService()
                    .getPsign(deploySign);

        List<Map> resList = new ArrayList<>();
        if(psign != null) {
            queryMap.put("psign",psign);
            resList = serviceFactory
                    .getAbstractService()
                    .getOverSpeedResult(queryMap);
        for(int i = 0; i < resList.size(); i ++)
            cnt = cnt + Integer.parseInt(resList.get(i).get("cnt").toString());
            totalCnt = serviceFactory
                    .getAbstractService()
                    .getOverSpeedTotalCnt(queryMap);
            total = serviceFactory
                    .getAbstractService()
                    .getOverSpeedPage(queryMap).size();
        }
        resultMap.put("result",resList);
        resultMap.put("cnt",cnt);
        resultMap.put("totalCnt",totalCnt);
        resultMap.put("total",total);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        return resultMap;
    }

    @GetMapping("getOverSpeedDetail")
    public HashMap getOverSpeedDetail(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap=new HashMap<>();
        Integer page=(request.getParameter("page")==null) ? 0 : Integer.parseInt(request.getParameter("page"));
        Integer count=(request.getParameter("count")==null) ? 10 : Integer.parseInt(request.getParameter("count"));
        String export = request.getParameter("export")==null ? "0" : request.getParameter("export");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String organno = request.getParameter("organno");
        String deploySign = request.getParameter("deploySign")+"_API";
        logger.info(request.getRemoteAddr() +" 访问：alarm/getOverSpeedResult,deploySign={},organno={},startTime={},endTime={}",deploySign,organno,startTime,endTime);
        queryMap.put("size",count);
        queryMap.put("offset",count*(page));
        queryMap.put("export",export);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("organno", organno);
        if(request.getParameter("carno") != null){
            queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        }
        if(request.getParameter("warnseconds")!=null){
            queryMap.put("warnseconds", request.getParameter("warnseconds"));
        }
        int total = 0;
        List<Map> resList = new ArrayList<>();
        String psign = serviceFactory
                .getAbstractService()
                .getPsign(deploySign);
        if(psign != null) {
            queryMap.put("psign",psign);
            resList = serviceFactory
                    .getAbstractService()
                    .getOverSpeedDetail(queryMap);
            total = serviceFactory
                    .getAbstractService()
                    .getOverSpeedDetailPage(queryMap);
        }
        resultMap.put("total",total);
        resultMap.put("result",resList);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        return resultMap;
    }

    @GetMapping("getWarningCnt")
    public HashMap getWarningCnt(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap=new HashMap();
        HashMap queryMap = new HashMap();
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        logger.info(request.getRemoteAddr() +" 访问：alarm/getOverSpeedResult,starttime={},endtime={}",starttime,endtime);
        queryMap.put("starttime", starttime);
        queryMap.put("endtime", endtime);
        queryMap.put("carno", Arrays.asList(request.getParameter("carno").split(",")));
        queryMap.put("organno", Arrays.asList(request.getParameter("organno").split(",")));
        queryMap.put("table", CommonUtils.getTable(request.getParameter("areaid")));
        int overspeedCount = serviceFactory
                .getAbstractService()
                .getOverSpeedCount(queryMap);
        int terminalCount = serviceFactory
                .getAbstractService()
                .getTermianlCount(queryMap);
        int fenceCount = serviceFactory
                .getAbstractService()
                .getFenceCount(queryMap);
        int drivingCount = serviceFactory
                .getAbstractService()
                .getDrivingCount(queryMap);
        resultMap.put("overspeedCount", overspeedCount);
        resultMap.put("terminalCount", terminalCount);
        resultMap.put("fenceCount",fenceCount);
        resultMap.put("drivingCount", drivingCount);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        return  resultMap;
    }

}
