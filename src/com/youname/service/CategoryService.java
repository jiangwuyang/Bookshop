package com.youname.service;

import java.sql.SQLException;
import java.util.List;

import com.youname.dao.CategoryDao;
import com.youname.domain.Category;

public class CategoryService {

	public List<Category> findAllCategory() {
		CategoryDao dao=new CategoryDao();
		List<Category> categoryList=null;
		try {
			categoryList = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryList;
	}

	public Category findAllCategoryByCid(String cid) {
		CategoryDao dao=new CategoryDao();
	    Category category=null;
		try {
			category = dao.findAllCategoryByCid(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return category;
	}

}
