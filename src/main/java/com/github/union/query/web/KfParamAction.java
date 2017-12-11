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

import com.github.union.query.models.KfParam;
import com.github.union.query.service.KfParamService;

/**
 * @author liuhaoyong on 2015/6/18.
 */
@Controller
@RequestMapping("/param")
public class KfParamAction extends AbstractAction {
    private static Logger logger = Logger.getLogger(KfParamAction.class);

    @Resource
    private KfParamService kfParamService;

    /**
     * http://localhost:8080/query/param/list.action?toPage=true
     * 列表
     * @param request
     * @param response
     * @param toPage
     * @param param
     * @param model
     * @return
     */
    @RequestMapping(method=RequestMethod.GET,value="/list")
    public String list(HttpServletRequest request,HttpServletResponse response,String toPage,KfParam param,Model model) throws UnsupportedEncodingException {
        boolean isPage = StringUtils.equalsIgnoreCase(toPage, "true");

        String json = kfParamService.tableJson(param, param.getsEcho(), kfParamService.search(param) );
        if(isPage){
            return "param/list";
        }
        this.renderText(response, json);
        return null;
    }


    @RequestMapping(method=RequestMethod.GET,value="/new")
    public String toInput(HttpServletRequest request,Integer id,Model model){
        KfParam com = new KfParam();
        if(id == null || id.intValue() == 0){
            model.addAttribute(com);
        }else{
            com = kfParamService.selectByPk(id);
            if(com == null){
                com = new KfParam();
            }
            model.addAttribute(com);
        }
        return "param/input";
    }

    /**
     * 保存
     * @param response
     * @param
     * @return
     */
    @RequestMapping(method=RequestMethod.POST,value="/save")
    public String save(HttpServletRequest request,HttpServletResponse response,KfParam pro){
        try{
            if(pro.getId()==null || pro.getId()<=0){
                kfParamService.insert(pro);
            }else{
                kfParamService.update(pro);
            }
            this.renderText(response, MESSAGE_OK);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.renderText(response, MESSAGE_ERROR);
        }
        return null;
    }
}

