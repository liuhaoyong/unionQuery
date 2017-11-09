package com.wac.query.web;

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

import com.wac.query.models.KfBusniess;
import com.wac.query.service.KfBusniessService;

/**
 * @author huangjinsheng on 2015/6/18.
 */
@Controller
@RequestMapping("/busniess")
public class KfBusniessAction extends AbstractAction {
    private static Logger logger = Logger.getLogger(KfBusniessAction.class);

    @Resource
    private KfBusniessService kfBusniessService;

    /**
     * http://localhost:8080/query/busniess/list.action?toPage=true
     * 列表
     * @param request
     * @param response
     * @param toPage
     * @param param
     * @param model
     * @return
     */
    @RequestMapping(method=RequestMethod.GET,value="/list")
    public String list(HttpServletRequest request,HttpServletResponse response,String toPage,KfBusniess param,Model model) throws UnsupportedEncodingException {
        boolean isPage = StringUtils.equalsIgnoreCase(toPage, "true");

        if(StringUtils.isNotBlank(param.getBusniessName())){
            param.setBusniessName(java.net.URLDecoder.decode(
                    StringUtils.defaultString(param.getBusniessName()),
                    "utf-8"));
        }else{
            param.setBusniessName(null);
        }
        String json = kfBusniessService.tableJson(param, param.getsEcho(), kfBusniessService.search(param) );
        if(isPage){
            return "busniess/list";
        }
        this.renderText(response, json);
        return null;
    }


    @RequestMapping(method=RequestMethod.GET,value="/new")
    public String toInput(HttpServletRequest request,Integer id,Model model){
        KfBusniess com = new KfBusniess();
        if(id == null || id.intValue() == 0){
            model.addAttribute(com);
        }else{
            com = kfBusniessService.selectByPk(id);
            if(com == null){
                com = new KfBusniess();
            }
            model.addAttribute(com);
        }
        return "busniess/input";
    }

    /**
     * 保存
     * @param response
     * @param
     * @return
     */
    @RequestMapping(method=RequestMethod.POST,value="/save")
    public String save(HttpServletRequest request,HttpServletResponse response,KfBusniess pro){
        try{
            if(pro.getId()==null || pro.getId()<=0){
                kfBusniessService.insert(pro);
            }else{
                kfBusniessService.update(pro);
            }
            this.renderText(response, MESSAGE_OK);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.renderText(response, MESSAGE_ERROR);
        }
        return null;
    }
}

