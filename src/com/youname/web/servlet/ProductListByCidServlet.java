package com.youname.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youname.domain.PageBean;
import com.youname.domain.Product;
import com.youname.service.ProductService;

public class ProductListByCidServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//获得cid
		String cid=request.getParameter("cid");
		String currentPageStr=request.getParameter("currentPage");
		if (currentPageStr==null) currentPageStr="1";
		int currentPage=Integer.parseInt(currentPageStr);	
		int currentCount=12;
		
		ProductService service=new ProductService();
		PageBean pageBean=service.findProductListByCid(cid,currentPage,currentCount);
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}