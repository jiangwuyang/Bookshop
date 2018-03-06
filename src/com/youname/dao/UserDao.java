package com.youname.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.youname.domain.User;
import com.youname.utils.DataSourceUtils;

public class UserDao {

	public int regist(User user) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="insert into user values(?,?,?,?,?,?,?,?,?,?)";
		int update=runner.update(sql, user.getUid(),user.getUsername(),user.getPassword()
				,user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode());
		return update;
	}

	public int active(String activeCode) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update user set state=? where code=?";
		int update=runner.update(sql, 1,activeCode);
		return update;
	}

	public long isExit(String username) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from user where username=?";
		Long isExit=(Long) runner.query(sql, new ScalarHandler(), username);
		return isExit;
	}

	public Long isLogin(String username, String password) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from user where username=? and password=?";
		Long isLogin=(Long) runner.query(sql, new ScalarHandler(), username,password);
		return isLogin;
	}

	public Long isState(String username, String password) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from user where username=? and password=? and state=1";
		Long isLogin=(Long) runner.query(sql, new ScalarHandler(), username,password);
		return isLogin;
	}

}
