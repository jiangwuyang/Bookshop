package com.youname.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.youname.domain.Category;
import com.youname.domain.Product;
import com.youname.utils.DataSourceUtils;

public class CategoryDao {

	public List<Category> findAllCategory() throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from Category";
		return runner.query(sql, new BeanListHandler<Category>(Category.class));
	}

}
