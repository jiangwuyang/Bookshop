package com.youname.service;

import java.sql.SQLException;
import java.util.List;

import com.youname.dao.ProductDao;
import com.youname.domain.PageBean;
import com.youname.domain.Product;

public class ProductService {
	//获得热门商品
	public List<Product> findHotProductList() {
		ProductDao dao=new ProductDao();
		List<Product> hotProductList=null;
		try {
			hotProductList=dao.findHotProductList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hotProductList;
	}
	//获得最新商品
	public List<Product> findNewProductList() {
		ProductDao dao=new ProductDao();
		List<Product> newProductList=null;
		try {
			newProductList=dao.findHotProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newProductList;
	}
	public PageBean findProductListByCid(String cid, int currentPage, int currentCount) {
		ProductDao dao=new ProductDao();
		//封装一个PageBean 返回web层
		PageBean<Product>  pageBean=new PageBean<Product>();
	
		
		//封装当前页
		pageBean.setCurrentPage(currentPage);
		//封装每页显示条数
		pageBean.setCurrentCount(currentCount);
		//封装总条数
		int totalCount=0;
		try {
			 totalCount=dao.getCount(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		
		//封装总页数
		int totalPage=(int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//当前页显示的数据
		//当前页与起始索引9index的关系
		int index=(currentPage-1)*currentCount;
		List<Product> list=null;
		try {
			list=dao.findProductByPage(cid,index,currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pageBean.setList(list);
		
		return pageBean;
	}

}
