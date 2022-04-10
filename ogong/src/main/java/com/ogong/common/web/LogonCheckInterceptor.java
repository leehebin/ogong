package com.ogong.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.ogong.service.domain.User;


public class LogonCheckInterceptor implements HandlerInterceptor{

	/*
	 * @Override public boolean preHandle(HttpServletRequest request,
	 * HttpServletResponse response, Object handler) throws Exception {
	 * 
	 * HttpSession session = request.getSession(); User loginusUser = (User)
	 * session.getAttribute("user"); System.out.println("인터셉터 : " + loginusUser);
	 * 
	 * if(loginusUser == null) { // 로그인이 안된 경우 로그인 폼으로 이동
	 * response.sendRedirect(request.getContextPath()+ "/user/loginView"); return
	 * false; }
	 * 
	 * return true; // 로그인 된 경우 다음 인터페이스나 페이지 컨트롤러를 실행 }
	 */

	  
		/*
		 * @Override public void postHandle(HttpServletRequest request,
		 * HttpServletResponse response, Object handler, ModelAndView modelAndView){
		 * 
		 * 
		 * HttpSession session = request.getSession(); User loginusUser = (User)
		 * session.getAttribute("user"); System.out.println("인터셉터 : " + loginusUser);
		 * 
		 * if (loginusUser == null) {
		 * 
		 * }
		 * 
		 * 
		 * }
		 */
}