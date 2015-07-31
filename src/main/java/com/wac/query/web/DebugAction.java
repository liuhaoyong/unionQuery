package com.wac.query.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangjinsheng on 2015/6/18.
 */
@Controller
@RequestMapping("/debug")
public class DebugAction extends AbstractAction {
    /**
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/ping")
    public void ping(HttpServletRequest request, HttpServletResponse response) {
        this.renderText(response, "i'm ok!");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/page")
    public String page(HttpServletRequest request, HttpServletResponse response,Model model) {
        model.addAttribute("t","ssssssssssssssssssssssssss");
        return "debug/test2";
    }
}
