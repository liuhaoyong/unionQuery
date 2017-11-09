package com.wac.query.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author tianwen
 * 
 */
@Controller
@RequestMapping("/exception")
public class ExceptionAction extends AbstractAction {

	@RequestMapping(value = "/nopage", method = RequestMethod.GET)
	public String pageNotFound(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		return "errors/404";
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String innerError(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		return "errors/500";
	}
}
