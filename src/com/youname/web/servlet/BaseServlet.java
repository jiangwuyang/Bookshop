package com.youname.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    req.setCharacterEncoding("utf-8");
		try {
			//获得请求的名字
			String methodName=req.getParameter("method");
			//获得当前被访问的对象的字节码对象
			Class clazz=this.getClass();
			//获得当前字节码对象中的指定方法
			Method method=clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
		    //执行相应的方法
			method.invoke(this,req, resp);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}