package com.github.union.query.web;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.union.query.enums.DriverTypeEnum;
import com.github.union.query.models.KfDatabaseSource;
import com.github.union.query.service.KfDatabaseSourceService;

/**
 * @author liuhaoyong on 2015/6/18.
 */
@Controller
@RequestMapping("/dbs")
public class KfDatabaseSourceAction extends AbstractAction {
    private static Logger logger = Logger.getLogger(KfDatabaseSourceAction.class);

    @Resource
    private KfDatabaseSourceService kfDatabaseSourceService;

    /**
     * 列表
     * @param request
     * @param response
     * @param toPage
     * @param param
     * @param model
     * @return
     */
    @RequestMapping(method=RequestMethod.GET,value="/list")
    public String list(HttpServletRequest request,HttpServletResponse response,String toPage,KfDatabaseSource param,Model model) throws UnsupportedEncodingException {
        boolean isPage = StringUtils.equalsIgnoreCase(toPage, "true");
        if(StringUtils.isNotBlank(param.getDbsName())){
            param.setDbsName(java.net.URLDecoder.decode(
                    StringUtils.defaultString(param.getDbsName()),
                    "utf-8"));
        }else{
            param.setDbsName(null);
        }
        String json = kfDatabaseSourceService.tableJson(param, param.getsEcho(), kfDatabaseSourceService.search(param) );
        if(isPage){
            return "dbs/list";
        }
        this.renderText(response, json);
        return null;
    }

    /**
     *
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(method=RequestMethod.GET,value="/new")
    public String toInput(HttpServletRequest request,Integer id,Model model){
        KfDatabaseSource com = new KfDatabaseSource();
        if(id == null || id.intValue() == 0){
            model.addAttribute(com);
        }else{
            com = kfDatabaseSourceService.selectByPk(id);
            if(com == null){
                com = new KfDatabaseSource();
            }
            model.addAttribute(com);
        }
        model.addAttribute("driverMap", DriverTypeEnum.Mysql.toMap());
        return "dbs/input";
    }

    /**
     * 保存
     * @param response
     * @return
     */
    @RequestMapping(method=RequestMethod.POST,value="/save")
    public String save(HttpServletRequest request,HttpServletResponse response,KfDatabaseSource pro){
        try{
            if(pro.getId()==null || pro.getId()<=0){
                kfDatabaseSourceService.insert(pro);
            }else{
                kfDatabaseSourceService.update(pro);
            }
            this.renderText(response, MESSAGE_OK);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.renderText(response, MESSAGE_ERROR);
        }
        return null;
    }
}

