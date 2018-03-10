package com.youname.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.youname.dao.ProductDao;
import com.youname.domain.Cart;
import com.youname.domain.CartItem;
import com.youname.domain.PageBean;
import com.youname.domain.Product;
import com.youname.utils.DataSourceUtils;

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
		//当前页与起始索引index的关系
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
	public Product findProductByPid(String pid) {
		ProductDao dao=new ProductDao();
		Product product=null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
	public List<Cart> selectCartByUserId(String uid) {
		ProductDao dao=new ProductDao();
		List<Cart> listCart=null;
		try {
			listCart=dao.selectCartByUserId(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCart;
	}
	public Map<String, CartItem> selectCartItemByCartId(String cartid) {
		ProductDao dao=new ProductDao();
		Map<String, CartItem> cartItems=null;
		try {
			cartItems = dao.selectCartItemByCartId(cartid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cartItems;
	}
	public void addToCart(Cart cart) {
		ProductDao dao=new ProductDao();
		try {
			//1、开启事务
			DataSourceUtils.startTransaction();
			//2、调用dao存储addCart表数据的方法
			dao.addCart(cart);
			//3、调用dao存储addCartItem表数据的方法
			dao.addCartItem(cart);
			
		} catch (SQLException e) {
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
