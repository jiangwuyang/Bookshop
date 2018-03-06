package com.youname.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youname.service.UserService;

public class ActiveServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//获取验证码
		String activeCode=request.getParameter("activeCode");
		UserService userService=new UserService();
		boolean isActive=userService.active(activeCode);
		if(isActive) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}else {
			response.sendRedirect(request.getContextPath()+"/activeFail.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}