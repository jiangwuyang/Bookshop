package com.youname.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youname.domain.Category;
import com.youname.domain.Product;
import com.youname.service.ProductService;
import com.youname.service.CategoryService;

public class IndexServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service=new ProductService();
		//CategoryService cService=new CategoryService();
		//准备热门商品
		List<Product> hotProductlist=service.findHotProductList();
		
		//准备最新商品
		List<Product> newProductlist=service.findNewProductList();
		
	/*	//准备分类数据
		List<Category> categoryList=cService.findAllCategory();
		
		
		request.setAttribute("categoryList", categoryList);*/
		request.setAttribute("hotProductlist", hotProductlist);
		request.setAttribute("newProductlist", newProductlist);
		//response.sendRedirect(request.getContextPath()+"/index.jsp");
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}