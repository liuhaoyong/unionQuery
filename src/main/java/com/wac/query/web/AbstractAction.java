package com.wac.query.web;

/**
 * 
 */

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangjinsheng
 *
 */
public abstract class AbstractAction {
	private static Logger logger = Logger.getLogger(AbstractAction.class);
	protected final String MESSAGE_OK="ok";
	protected final String MESSAGE_ERROR="error";
	 /**
     * 设置浏览器输出头
     */
    protected void setCacheHeader(HttpServletResponse response,long mills) {
        long now = new Date().getTime();
        response.setDateHeader("Expires", now + mills);
        response.setDateHeader("Last-Modified", now);
        response.setHeader("Cache-Control", "max-age=" + (mills / 1000));

    }

    protected void setNoCacheHeader(HttpServletResponse response) {
        response.setHeader("Pragma", "No-Cache");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setDateHeader("Expires", 0);
    }
    
    /**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:UTF-8"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:UTF-8",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	protected void render(HttpServletResponse response,
			final String contentType, final String content,final String _encoding) {
		try {
			response.setCharacterEncoding(_encoding);
			response.setContentType(contentType+";charset="+_encoding);
		if(StringUtils.isNotBlank(content)){
				response.setContentLength(content.getBytes(_encoding).length);
			}
			response.getWriter().write(content);
			response.getWriter().flush();

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 直接输出文本.
	 * 
	 * @see #render(String, String, String...)
	 */
	protected void renderText(HttpServletResponse response,
			final String text) {
		render(response, "text/plain", text,"UTF-8");
	}

	/**
	 * 直接输出HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	protected void renderHtml(HttpServletResponse response,
			final String html, final String... headers) {
		render(response, "text/html", html, "UTF-8");
	}

	/**
	 * 直接输出XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	protected void renderXml(HttpServletResponse response,
			final String xml, final String... headers) {
		render(response, "text/xml", xml, "UTF-8");
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param string
	 *            json字符串.
	 * @see #render(String, String, String...)
	 */
	protected void renderJson(HttpServletResponse response,
			final String string, final String... headers) {
		render(response, "application/json", string, "utf-8");
	}
	
}
