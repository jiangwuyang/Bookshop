package com.youname.service;

import java.sql.SQLException;

import com.youname.dao.UserDao;
import com.youname.domain.User;

public class UserService {

	public boolean regist(User user) {
		UserDao dao=new UserDao();
		int row=0;
		try {
			row = dao.regist(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row>0?true:false;
	}

	public boolean active(String activeCode) {
		UserDao dao=new UserDao();
		int row=0;
		try {
			row=dao.active(activeCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row>0?true:false;
	}

	public boolean isExit(String username) {
		UserDao dao=new UserDao();
		Long isExit=0L;
		try {
			isExit=dao.isExit(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExit>0?true:false;
		
	}

	public User isLogin(String username, String password) {
		UserDao dao=new UserDao();
		User user=null;
		try {
			user=dao.isLogin(username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean isState(String username, String password) {
		UserDao dao=new UserDao();
		Long isLogin=0L;
		try {
			isLogin=dao.isState(username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isLogin>0?true:false;
	}

}
