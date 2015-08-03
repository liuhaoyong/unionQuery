package com.wac.query.web;

import java.io.UnsupportedEncodingException;
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

import com.wac.query.models.KfBusniess;
import com.wac.query.models.KfQuery;
import com.wac.query.models.QueryRelatedInfo;
import com.wac.query.service.KfBusniessService;
import com.wac.query.service.KfDatabaseSourceService;
import com.wac.query.service.KfParamService;
import com.wac.query.service.KfQueryService;
import com.wac.query.service.KfSqlService;

/**
 * @author huangjinsheng on 2015/6/18.
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
                       String toPage,
           KfQuery kfQuery, Model model) throws UnsupportedEncodingException {
        boolean isPage = StringUtils.equalsIgnoreCase(toPage, "true");
        if (isPage) {
            model.addAttribute("busniessList", kfBusniessService.all(new KfBusniess()));
            return "query/list";
        }
        if (StringUtils.isNotBlank(kfQuery.getParamValue())) {
            kfQuery.setParamValue(java.net.URLDecoder.decode(
                    StringUtils.defaultString(kfQuery.getParamValue()),
                    "utf-8"));
        }
        
		String json = kfQueryService.query(kfQuery.getBid(),kfQuery.getParamId(),kfQuery.getParamValue());
		logger.info(String.format("Query result:[%s]", StringUtils.abbreviate(json, 50)));
		if(logger.isDebugEnabled()){
			logger.debug(String.format("Query result:[%s]", json));
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
            StringBuilder sb = new StringBuilder();
            info.getAggregatedParams().entrySet().stream().forEach(map -> {
                sb.append("<option value='"+map.getKey()+"'>").append(map.getValue().getParamName()).append("</option>");
            });
            this.renderText(response,sb.toString());
        } catch (ExecutionException e) {
            logger.error(e.getMessage(),e);
            this.renderText(response,"error");
        }
    }


}

