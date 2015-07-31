package com.wac.query.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.wac.query.enums.SqlStatusEnum;
import com.wac.query.models.KfBusniess;
import com.wac.query.models.KfDatabaseSource;
import com.wac.query.models.KfParam;
import com.wac.query.models.KfSql;
import com.wac.query.models.KfSqlParam;
import com.wac.query.service.KfBusniessService;
import com.wac.query.service.KfDatabaseSourceService;
import com.wac.query.service.KfParamService;
import com.wac.query.service.KfQueryService;
import com.wac.query.service.KfSqlService;
import com.wac.query.utils.JsonTool;

/**
 * @author huangjinsheng on 2015/6/18.
 */
@Controller
@RequestMapping("/sql")
public class KfSqlAction extends AbstractAction {
	private static Logger logger = LoggerFactory.getLogger(KfQueryService.class);

    @Resource
    private KfSqlService kfSqlService;
    @Resource
    private KfBusniessService kfBusniessService;
    @Resource
    private KfDatabaseSourceService kfDatabaseSourceService;
    @Resource
    private KfParamService kfParamService;

    /**
     * 列表
     *
     * @param request
     * @param response
     * @param toPage
     * @param param
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response, String toPage, KfSql param, Model model) throws UnsupportedEncodingException {
        boolean isPage = StringUtils.equalsIgnoreCase(toPage, "true");
        if (StringUtils.isNotBlank(param.getSqlName())) {
            param.setSqlName(java.net.URLDecoder.decode(
                    StringUtils.defaultString(param.getSqlName()),
                    "utf-8"));
        } else {
            param.setSqlName(null);
        }
        param.setSqlStatus(SqlStatusEnum.正常.toInt());
        String json = kfSqlService.tableJson(param, param.getsEcho(), kfSqlService.search(param));
        if (isPage) {
            model.addAttribute("busniessList", kfBusniessService.all(new KfBusniess()));
            model.addAttribute("dbsList", kfDatabaseSourceService.all(new KfDatabaseSource()));
            return "sql/list";
        }
        this.renderText(response, json);
        return null;
    }

    /**
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public String toInput(HttpServletRequest request, Integer id, Model model) {
        KfSql com = new KfSql();
        if (id == null || id.intValue() == 0) {
            id = 0;
            model.addAttribute(com);
        } else {
            com = kfSqlService.selectByPk(id);
            if (com == null) {
                com = new KfSql();
            }
            model.addAttribute(com);
        }
        model.addAttribute("busniessList", kfBusniessService.all(new KfBusniess()));
        model.addAttribute("dbsList", kfDatabaseSourceService.all(new KfDatabaseSource()));
        model.addAttribute("paramList", com.getParams());
        model.addAttribute("statusMap", SqlStatusEnum.停用.toMap());
        return "sql/input";
    }

    /**
     * 保存
     *
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public String save(HttpServletRequest request, HttpServletResponse response, KfSql pro) {
        try {
            if (pro.getId() == null || pro.getId() <= 0) {
                kfSqlService.insert(pro);
            } else {
                kfSqlService.update(pro);
            }
            this.renderText(response, MESSAGE_OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.renderText(response, MESSAGE_ERROR);
        }
        return null;
    }

    /**
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/paramNew")
    public String toParamInput(HttpServletRequest request, Integer id, Model model) {
        KfSql com = kfSqlService.selectByPk(id);
        model.addAttribute("kfSql", com);
        List<KfParam> paramList = kfParamService.all(new KfParam());
        model.addAttribute("paramList", paramList);
        return "sql/paramInput";
    }

    /**
     *
     * @param request
     * @param response
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/saveParam")
    public String saveParam(HttpServletRequest request,
                            HttpServletResponse response,
                            Integer sqlId,
                            String[] ids,
                            String[] paramId,
                            String[] sqlField,
                            String[] paramDesc) {
        try {
            logger.info("sqlId");
            logger.info("String[] ids:"+ids==null?"null": JsonTool.writeValueAsString(ids));
            logger.info("String[] paramId:"+paramId==null?"null": JsonTool.writeValueAsString(paramId));
            logger.info("String[] sqlField:"+sqlField==null?"null": JsonTool.writeValueAsString(sqlField));
            logger.info("String[] paramDesc:" + paramDesc == null ? "null" : JsonTool.writeValueAsString(paramDesc));

            if(sqlId == null || sqlId.intValue() <= 0){
                throw new NullPointerException("sqlId is null");
            }

            List<KfSqlParam> paramList = new ArrayList();
            Date now = new Date(System.currentTimeMillis());
            for (int i=0;i<paramId.length;i++) {
                if(StringUtils.isBlank(paramId[i]) || Integer.parseInt(paramId[i]) <= 0){
                    continue;
                }
                KfSqlParam p = new KfSqlParam();
                p.setParamId(Integer.valueOf(paramId[i]));
                if(!StringUtils.endsWithIgnoreCase(paramDesc[i],"defaultXXX")){
                    p.setParamDesc(paramDesc[i]);
                }
                p.setSqlId(sqlId);
                p.setId(Integer.valueOf(ids[i]));
                if(!StringUtils.endsWithIgnoreCase(sqlField[i],"defaultXXX")){
                    p.setSqlField(sqlField[i]);
                }else{
                    KfParam kp = kfParamService.selectByPk(p.getParamId());
                    p.setSqlField(kp!=null?kp.getFieldName():null);
                }

                p.setCreateTime(now);
                paramList.add(p);
            }

            kfSqlService.saveParams(paramList,sqlId);
            this.renderText(response, MESSAGE_OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.renderText(response, MESSAGE_ERROR);
        }
        return null;
    }
}

