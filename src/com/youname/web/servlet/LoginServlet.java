package com.youname.web.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Session;

import com.youname.dao.UserDao;
import com.youname.service.UserService;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  request.setCharacterEncoding("utf-8");  
	        response.setContentType("text/html;charset=utf-8");  
	        String username = request.getParameter("username");  
	        String password = request.getParameter("password");  
	        String verifyc  = request.getParameter("checkCode");//<span style="font-family: Arial, Helvetica, sans-serif;"></span>//得到表单输入的内容  
	        String svc =(String) request.getSession().getAttribute("sessionverify");  
	        request.getSession().setAttribute("entryusername", username);
	        UserService userService=new UserService();
	        boolean isLogin=userService.isLogin(username,password);
	        boolean isState=userService.isState(username,password);
	        if(username ==""){  
	            request.setAttribute("msg1", "请输入用户名");  
	            request.getRequestDispatcher("/login.jsp").forward(request, response);  
	            return;  
	        }
	        if(password ==""){  
	            request.setAttribute("msg2", "请输入密码");  
	            request.getRequestDispatcher("/login.jsp").forward(request, response);  
	            return;  
	        } 
	        if (verifyc=="") {
	        	request.setAttribute("msg3", "请输入验证码");  
	            request.getRequestDispatcher("/login.jsp").forward(request, response);  
	            return;  
			}
	        if(!svc.equalsIgnoreCase(verifyc)){  
	            request.setAttribute("msg3", "验证码错误！");  
	            request.getRequestDispatcher("/login.jsp").forward(request, response);  
	            request.getSession().setAttribute("entrypassword", password);
	            return;  
	        }  
	       
	        if(!isLogin){  
	            request.setAttribute("msg", "用户名或密码错误请重新输入！");  
	            request.getRequestDispatcher("/login.jsp").forward(request, response);  
	            return;  
	        }
	        if (isLogin&&isState==false) {
				response.sendRedirect(request.getContextPath()+"/goactive.jsp");
			}
	        if(isLogin&&isState){   
	        	response.sendRedirect(request.getContextPath()+"/index.jsp");
	        	request.getSession().setAttribute("loginusername", username);
	        	return; 
	        }  
	          
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}