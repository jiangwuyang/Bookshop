package com.youname.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.youname.domain.Category;
import com.youname.service.CategoryService;
import com.youname.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

public class CategoryListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryService cService=new CategoryService();
		//先从缓存中查询categoryList 如果有直接使用没有在从数据库差
	    //获得jedis对象连接redis数据库
		Jedis jedis=JedisPoolUtils.getJedis();
		String categoryListJson=jedis.get("categoryListJson");
		if (categoryListJson==null) {
			System.out.println("redis没有数据,查询数据库");
			//准备分类数据
			List<Category> categoryList=cService.findAllCategory();
			Gson gson=new Gson();
			categoryListJson=gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);
		}

		response.setContentType("Text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}