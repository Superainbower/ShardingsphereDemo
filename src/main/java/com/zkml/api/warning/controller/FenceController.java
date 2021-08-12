package com.zkml.api.warning.controller;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("fence")
public class FenceController {
    private static final Logger logger = LoggerFactory.getLogger(FenceController.class);
    @Resource
    ServiceFactory serviceFactory;

    @GetMapping("query")
    public HashMap query(HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap = new HashMap();
        HashMap parmap = new HashMap();
        List<HashMap> result = new ArrayList<HashMap>();
        boolean useIdCard = false;
        String areaid = "0100102";
        int count = request.getParameter("count") == null?10:Integer.valueOf(request.getParameter("count"));
        int page = request.getParameter("page") == null?0:Integer.valueOf(request.getParameter("page"));
        String export = request.getParameter("export") == null?"0":request.getParameter("export");
        String type = request.getParameter("type");
        if(request.getParameter("useidcard") != null && request.getParameter("useidcard").equals("1")){
            useIdCard = true;
            parmap.put("useIdCard",useIdCard);
        }
        parmap.put("size",count);
        parmap.put("offset",count*(page));
        parmap.put("export",export);
        parmap.put("startTime",request.getParameter("startTime"));
        parmap.put("endTime", request.getParameter("endTime"));
        parmap.put("typeList",Arrays.asList(type.split("\\|")));
        if(request.getParameter("carno") != null){
            parmap.put("carnoList", Arrays.asList(request.getParameter("carno").split("\\|")));
        }
        if(request.getParameter("organ") != null){
            parmap.put("organList",Arrays.asList(request.getParameter("organ").split("\\|")));
        }
        if(request.getParameter("carlist") != null){
            parmap.put("nocarnolist",Arrays.asList(request.getParameter("carlist").split(",")));
        }
        if(request.getParameter("areaid") != null){
            areaid = request.getParameter("areaid").substring(0,7);
        }
        if(request.getParameter("carno") != null){
            List<Map> tvmappingList =  serviceFactory
                    .getAbstractService()
                    .findAreaIdAndidcardByCarIds((Arrays.asList(request.getParameter("carno").split("\\|"))));
            if(tvmappingList.size() == 0) {
                resultMap.put("totalCnt",0);
                resultMap.put("retCode",10002);
                resultMap.put("des", "车辆不存在");
                resultMap.put("result", result);
                return resultMap;
            }
            areaid = tvmappingList.get(0).get("areaid").toString().substring(0,7);
            if(useIdCard){
                List<String> idcardList = new ArrayList<>();
                for(int i = 0; i < tvmappingList.size();i++){
                    idcardList.add(tvmappingList.get(i).get("idcard").toString());
                }
                parmap.put("idcardList",idcardList);
            }
        }else{
            if(useIdCard){
                List<String> idcards =  serviceFactory
                        .getAbstractService()
                        .findidcardByOrangList(Arrays.asList(request.getParameter("organ").split("\\|")));
                if( idcards.size() == 0){
                    resultMap.put("totalCnt",0);
                    resultMap.put("retCode", 10002);
                    resultMap.put("des", "车辆不存在");
                    resultMap.put("result", result);
                    return resultMap;
                }
                parmap.put("idcardList",idcards);
            }
        }
        parmap.put("table", CommonUtils.getTable(areaid));
        logger.info(request.getRemoteAddr() +" 访问：fence/query,parmap:{},",parmap);
        int totalCnt =  serviceFactory
                .getAbstractService()
                .getCnt(parmap);
        List<BaojingEntity> listbao =  serviceFactory
                .getAbstractService()
                .getList(parmap);
        parmap.put("status","-1");
        int Cnt =  serviceFactory
                .getAbstractService()
                .getCnt(parmap);
        result = formatMap(listbao,type);
        resultMap.put("Cnt",Cnt);
        resultMap.put("totalCnt",totalCnt);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        resultMap.put("result", result);
        return resultMap;
    }

    @GetMapping("queryPosition")
    public HashMap queryPosition(HttpServletRequest request,HttpServletResponse response){
        HashMap resultMap = new HashMap();
        HashMap parmap = new HashMap();
        String type = request.getParameter("type") == null?"9":request.getParameter("type");
        int count = request.getParameter("count") == null?10:Integer.valueOf(request.getParameter("count"));
        int page = request.getParameter("page") == null?0:Integer.valueOf(request.getParameter("page"));
        String export = request.getParameter("export") == null?"0":request.getParameter("export");
        String supervise = request.getParameter("supervise")==null?"0":request.getParameter("supervise");
        parmap.put("type",type);
        parmap.put("size",count);
        parmap.put("offset",count*(page));
        parmap.put("export",export);
        parmap.put("startTime",request.getParameter("startTime"));
        parmap.put("endTime", request.getParameter("endTime"));
        if(request.getParameter("status") != null){
            String status = request.getParameter("status");
            if(status.equals("0")) {
                parmap.put("status", "未处理");
            }else if(status.equals("1")) {
                parmap.put("status", "已处理");
            }
        }
        if( supervise.equals("1")) {
            if (request.getParameter("carno") != null) {
                parmap.put("carnoList", Arrays.asList(request.getParameter("carno").split("\\|")));
            }
        }else{
            if (request.getParameter("carno") != null) {
                parmap.put("carno",request.getParameter("carno"));
            }
        }
        if(request.getParameter("organ") != null){
            parmap.put("organList",Arrays.asList(request.getParameter("organ").split("\\|")));
        }
        logger.info(request.getRemoteAddr() +" 访问：fence/queryPosition,paramp:{}",parmap);

        int totalCnt =  serviceFactory
                .getAbstractService()
                .getPositionCnt(parmap);
        List<BaojingEntity> listbao =  serviceFactory
                .getAbstractService()
                .getPositionList(parmap);
        parmap.put("status", "未处理");
        int Cnt =  serviceFactory
                .getAbstractService()
                .getPositionCnt(parmap);
        List<HashMap> result = formatMap(listbao,type);
        resultMap.put("Cnt",Cnt);
        resultMap.put("totalCnt",totalCnt);
        resultMap.put("retCode", 0);
        resultMap.put("des", "成功");
        resultMap.put("result", result);
        return resultMap;
    }

    @GetMapping("dealAlarm")
    public HashMap dealAlarm(HttpServletRequest request,HttpServletResponse response){
        logger.info(request.getRemoteAddr() +" 访问：fence/dealAlarm");
        serviceFactory
                .getAbstractService()
                .dealAlarm(foramtDealMap(request));
        HashMap map = new HashMap();
        map.put("retCode", 0);
        map.put("des", "成功");
        return map;
    }

    @GetMapping("dealfenceAlarm")
    public HashMap dealfenceAlarm(HttpServletRequest request,HttpServletResponse response){
        logger.info(request.getRemoteAddr() +" 访问：fence/dealfenceAlarm");
        serviceFactory
                .getAbstractService()
                .dealfenceAlarm(foramtDealMap(request));
        HashMap map = new HashMap();
        map.put("retCode", 0);
        map.put("des", "成功");
        return map;
    }

    public HashMap foramtDealMap(HttpServletRequest request){
        HashMap parmap = new HashMap();
        String id = request.getParameter("id");
        String person = request.getParameter("person");
        String dealtime = request.getParameter("dealtime");
        String opinion = request.getParameter("opinion");
        String daqtime = request.getParameter("daqtime");
        if(request.getParameter("areaid") != null)
            parmap.put("table", CommonUtils.getTable(request.getParameter("areaid")));
        parmap.put("id",id);
        parmap.put("person",person);
        parmap.put("dealtime",dealtime);
        parmap.put("opinion",opinion);
        parmap.put("daqtime",daqtime);
        return parmap;
    }

    public List<HashMap> formatMap(List<BaojingEntity> listbao,String type){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        List<HashMap> result = new ArrayList<HashMap>();
        for (int i = 0; i < listbao.size(); i++) {
            HashMap resMap = new HashMap();
            resMap.put("id", listbao.get(i).getId());
            resMap.put("carno", listbao.get(i).getCarno());
            resMap.put("content", listbao.get(i).getContent());
            resMap.put("daq_time", listbao.get(i).getDaq_time());
            resMap.put("latitude", listbao.get(i).getLatitude());
            resMap.put("longitude", listbao.get(i).getLongitude());
            resMap.put("organno", listbao.get(i).getOrganno());
            resMap.put("qy_id", listbao.get(i).getQy_id());
            resMap.put("type", listbao.get(i).getType());
            resMap.put("address", listbao.get(i).getAddress());
            resMap.put("idcard", listbao.get(i).getIdcard());
            resMap.put("endTime", listbao.get(i).getEnd_time());
            resMap.put("status", listbao.get(i).getStatus());
            resMap.put("person", listbao.get(i).getPerson());
            resMap.put("dealtime", listbao.get(i).getDealtime());
            resMap.put("opinion", listbao.get(i).getOpinion());
            resMap.put("name",  serviceFactory
                    .getAbstractService()
                    .findNameByfenceId(listbao.get(i).getQy_id()));
            if(type.equals("9") || type.equals("10")) {
                try {
                    Date currdate = sf.parse(listbao.get(i).getCreate_time().split(" ")[0]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currdate);
                    if (type.equals("10"))
                        calendar.add(Calendar.DATE, -1);
                    resMap.put("createTime", sf.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                resMap.put("msg_status",listbao.get(i).getMsg_status());
            }
            result.add(resMap);
        }
        return result;
    }
}



