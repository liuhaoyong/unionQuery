package com.github.union.query.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.union.query.models.KfBusniess;
import com.github.union.query.models.KfMultiQuery;
import com.github.union.query.models.KfSingleQuery;
import com.github.union.query.models.QueryRelatedInfo;
import com.github.union.query.service.KfBusniessService;
import com.github.union.query.service.KfDatabaseSourceService;
import com.github.union.query.service.KfParamService;
import com.github.union.query.service.KfQueryService;
import com.github.union.query.service.KfSqlService;

/**
 * @author liuhaoyong on 2015/6/18.
 */
@Controller
@RequestMapping("/q")
public class KfQueryAction extends AbstractAction {
	private static Logger logger = LoggerFactory.getLogger(KfQueryAction.class);

    @Resource
    private KfSqlService kfSqlService;
    @Resource
    private KfBusniessService kfBusniessService;
    @Resource
    private KfDatabaseSourceService kfDatabaseSourceService;
    @Resource
    private KfParamService kfParamService;
    @Resource
    private KfQueryService kfQueryService;
    
    
    /**
    *
    * @param request
    * @param response
    * @param kfQuery
    * @param model
    * @return
    * @throws UnsupportedEncodingException
    */
   @RequestMapping(method = RequestMethod.GET, value = "/list")
   public String list(HttpServletRequest request, HttpServletResponse response,
          KfMultiQuery kfQuery, Model model) throws UnsupportedEncodingException {
       List<KfBusniess> kfBusniessList = getKfBusniesses();
	   model.addAttribute("busniessList", kfBusniessList);
       return "query/list";
   }

    private List<KfBusniess> getKfBusniesses() {
        List<KfBusniess> allBusinessList = kfBusniessService.all(new KfBusniess());
        List<KfBusniess> kfBusniessList = new ArrayList<>();
        for (KfBusniess kfBusniess : allBusinessList) {
                kfBusniessList.add(kfBusniess);
        }
        return kfBusniessList;
    }

    /**
     *
     * @param request
     * @param response
     * @param kfQuery
     * @param model
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/multiList")
    public String multiList(HttpServletRequest request, HttpServletResponse response,
           KfMultiQuery kfMultiQuery, Model model) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(kfMultiQuery.getParamValue())) {
        	kfMultiQuery.setParamValue(java.net.URLDecoder.decode(
                    StringUtils.defaultString(kfMultiQuery.getParamValue()),
                    "utf-8"));
        }
        
		String json = kfQueryService.query(kfMultiQuery.getBid(),kfMultiQuery.getParamId(),kfMultiQuery.getParamValue());
		logger.info(String.format("multiQuery result:[%s]", StringUtils.abbreviate(json, 50)));
		if(logger.isDebugEnabled()){
			logger.debug(String.format("multiQuery result:[%s]", json));
		}
		this.renderText(response, json);
        return null;
    }
    
    /**
    *
    * @param request
    * @param response
    * @param kfQuery
    * @param model
    * @return
    * @throws UnsupportedEncodingException
    */
   @RequestMapping(method = RequestMethod.GET, value = "/singleList")
   public String singleList(HttpServletRequest request, HttpServletResponse response,
		   KfSingleQuery kfSingleQuery, Model model) throws UnsupportedEncodingException {
       if (kfSingleQuery.getParamValues() != null && kfSingleQuery.getParamValues().length > 0) {
    	   for (int i = 0;i< kfSingleQuery.getParamValues().length ;i++) {
    		   kfSingleQuery.getParamValues()[i] = java.net.URLDecoder.decode(
                       StringUtils.defaultString(kfSingleQuery.getParamValues()[i]),
                       "utf-8");
    	   }
           
       }
       
		String json = kfQueryService.singleQuery(kfSingleQuery);
		logger.info(String.format("singleQuery result:[%s]", StringUtils.abbreviate(json, 50)));
		if(logger.isDebugEnabled()){
			logger.debug(String.format("singleQuery result:[%s]", json));
		}
		this.renderText(response, json);
       return null;
   }

    /**
     *
     * @param request
     * @param busniessId
     * @param model
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getParams")
    public void getParams(HttpServletRequest request,HttpServletResponse response, int busniessId, Model model) {
        try {
            QueryRelatedInfo info = kfQueryService.getQueryRelatedInfo(busniessId);
            JSONObject json = new JSONObject();
            StringBuilder sb = new StringBuilder();
            if(info.getSqls().size() > 1 || info.getSqls().size() <= 0){ //多条sql不支持分页和多条件查询等特性
            	sb.append("<select id=\"paramId\" class=\"s_select\">");
            	info.getAggregatedParams().entrySet().stream().forEach(map -> {
            		sb.append("<option value='"+map.getKey()+"'>").append(map.getValue().getParamName()).append("</option>");
                });
            	sb.append("</select>");
            	json.put("type", "multi");
            }else{
            	info.getAggregatedParams().entrySet().stream().forEach(map -> {
            		sb.append("<tr name=\"singleParams\">")
            		.append("<td>").append(map.getValue().getParamName()).append("</td>")
            		.append("<td>").append("<input type='text' size='40' name='param_ids_' id='param_"+map.getKey()+"'>")
            		.append("</td>")
            		.append("</tr>");
                });
            	json.put("type", "single");
            	
            	//分页表
            	json.put("table", kfQueryService.getSingleParamTableTitle(info));
            }
            json.put("html", sb.toString());
            this.renderText(response,json.toJSONString());
        } catch (ExecutionException e) {
            logger.error(e.getMessage(),e);
            this.renderText(response,"error");
        }
    }


}

