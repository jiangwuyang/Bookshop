package com.youname.web.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youname.utils.VerifyCode;

public class CheckCodeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		VerifyCode vc = new VerifyCode();  
        BufferedImage image = vc.getImage(80,30);//设置验证码图片大小  
        request.getSession().setAttribute("sessionverify", vc.getText());//将验证码文本保存到session域  
        VerifyCode.output(image, response.getOutputStream());  
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}