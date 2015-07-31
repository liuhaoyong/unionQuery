package com.wac.query.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 移动服务接口
 * 
 * @author jpeng
 * 
 */
@Controller
@RequestMapping("/index")
public class IndexAction extends AbstractAction {
	private static Logger logger = Logger.getLogger(IndexAction.class);
	
	
	/**
	 * 片库查询
	 * http://127.0.0.1:50001/s3portal/service/pianku/condition.action?firstTagId=&deviceType=3&tagType=1
	 * @param request
	 * @param response
	 * @param core
	 * @param searchParams
	 */
	@RequestMapping(method=RequestMethod.GET,value="/frame")
	public String frame(HttpServletRequest request,HttpServletResponse response,Model model){
		request.getSession().setAttribute("source", "query");//在session保存来源是来自query，用于嵌入现有bops功能
		return "frame";
	}
	
	/**
	 * 片库查询
	 * http://127.0.0.1:50001/s3portal/service/pianku/condition.action?firstTagId=&deviceType=3&tagType=1
	 * @param request
	 * @param response
	 * @param core
	 * @param searchParams
	 */
	@RequestMapping(method=RequestMethod.GET,value="/index")
	public String index(HttpServletRequest request,HttpServletResponse response){
		return "index";
	}
	
	
	/**
	 * http://127.0.0.1:50001/s3portal/service/pianku/ping.action
	 * @param request
	 * @param response
	 * @param core
	 * @param searchParams
	 */
	@RequestMapping(value="/ping")
	public void ping(HttpServletRequest request,HttpServletResponse response){
		this.renderText(response, "i'm alive!");
	}
}
