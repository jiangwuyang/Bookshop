package com.youname.web.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.youname.domain.Product;
import com.youname.domain.User;
import com.youname.service.ProductService;
import com.youname.service.UserService;
import com.youname.utils.CommonsUtils;
import com.youname.utils.MailUtils;
import com.youname.utils.VerifyCode;

public class UserServlet extends BaseServlet {

	/*
	 * public void doGet(HttpServletRequest request, HttpServletResponse response)
	 * throws ServletException, IOException { //获取请求那个方法的method
	 * request.setCharacterEncoding("utf-8"); String
	 * methodName=request.getParameter("method");
	 * 
	 * if ("active".equals(methodName)) { active(request,response); }else if
	 * ("checkCode".equals(methodName)) { checkCode(request,response); }else if
	 * ("login".equals(methodName)) { login(request,response); }else if
	 * ("quitUser".equals(methodName)) { quitUser(request,response); }else if
	 * ("register".equals(methodName)) { register(request,response); }else if
	 * ("checkUserName".equals(methodName)) { checkUserName(request,response); } }
	 * 
	 * public void doPost(HttpServletRequest request, HttpServletResponse response)
	 * throws ServletException, IOException { doGet(request, response); }
	 */

	// 激活功能
	public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 获取验证码
		String activeCode = request.getParameter("activeCode");
		UserService userService = new UserService();
		boolean isActive = userService.active(activeCode);
		if (isActive) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		} else {
			response.sendRedirect(request.getContextPath() + "/activeFail.jsp");
		}
	}

	// 检查验证码
	public void checkCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		VerifyCode vc = new VerifyCode();
		BufferedImage image = vc.getImage(80, 30);// 设置验证码图片大小
		request.getSession().setAttribute("sessionverify", vc.getText());// 将验证码文本保存到session域
		VerifyCode.output(image, response.getOutputStream());
	}

	// 登录功能
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verifyc = request.getParameter("checkCode");// <span style="font-family: Arial, Helvetica,
															// sans-serif;"></span>//得到表单输入的内容
		String svc = (String) request.getSession().getAttribute("sessionverify");
		String tocart = (String) request.getSession().getAttribute("tocart");
		request.getSession().setAttribute("entryusername", username);
		UserService userService = new UserService();
		User user = userService.isLogin(username, password);
		boolean isState = userService.isState(username, password);
		if (username == "") {
			request.setAttribute("msg1", "请输入用户名");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		if (password == "") {
			request.setAttribute("msg2", "请输入密码");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		if (verifyc == "") {
			request.setAttribute("msg3", "请输入验证码");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		if (!svc.equalsIgnoreCase(verifyc)) {
			request.setAttribute("msg3", "验证码错误！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			request.getSession().setAttribute("entrypassword", password);
			return;
		}

		if (user == null) {
			request.setAttribute("msg", "用户名或密码错误请重新输入！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		if (user != null && isState == false) {
			response.sendRedirect(request.getContextPath() + "/goactive.jsp");
		}
		if (user != null && isState && tocart == "购物车跳过来的") {
			String url = "/product?method=productInfo&pid=" + request.getSession().getAttribute("pid") + "&cid="
					+ request.getSession().getAttribute("cid") + "&currentPage="
					+ request.getSession().getAttribute("currentPage") + "";
			request.getSession().setAttribute("user", user);
			request.getRequestDispatcher(url).forward(request, response);
			return;
		}
		if (user != null && isState && tocart == null) {
			ProductService service = new ProductService();
			// CategoryService cService=new CategoryService();
			// 准备热门商品
			List<Product> hotProductlist = service.findHotProductList();

			// 准备最新商品
			List<Product> newProductlist = service.findNewProductList();

			/*
			 * //准备分类数据 List<Category> categoryList=cService.findAllCategory();
			 * 
			 * 
			 * request.setAttribute("categoryList", categoryList);
			 */
			request.setAttribute("hotProductlist", hotProductlist);
			request.setAttribute("newProductlist", newProductlist);
			request.getSession().setAttribute("user", user);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}

	}

	// 退出功能
	public void quitUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("tocart");
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}

	// 注册功能
	public void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// 获得表单数据
		Map<String, String[]> parameterMap = request.getParameterMap();
		User user = new User();

		try {
			// 自己定义一个类型转换器（将string转化成date）
			ConvertUtils.register(new Converter() {

				@Override
				public Object convert(Class clazz, Object value) {
					// 将string转化为date
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date parse = null;
					try {
						parse = format.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}

			}, Date.class);
			// 映射封装
			BeanUtils.populate(user, parameterMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// private String uid;
		user.setUid(CommonsUtils.getUUID());
		// private String telephone;
		user.setTelephone(null);
		// private int state;//是否激活
		user.setState(0);
		// private String code;//激活码
		String activeCode = CommonsUtils.getUUID();
		user.setCode(activeCode);

		System.out.println(user.toString());

		// 将user传递给service层
		UserService service = new UserService();
		boolean isRegisterSuccess = service.regist(user);
		// 是否注册成功
		if (isRegisterSuccess) {
			// 发送激活邮件
			// 发送激活邮件
			String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户";
			/*
			 * + "<a href='http://localhost:8080/Bookshop/user?method=active?activeCode="
			 * +activeCode+"'>" +"注册成功"+"</a>";
			 */
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

			// 跳转到注册成功页面
			response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
		} else {
			// 跳转到失败的提示页面
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}
	}

	// 检验用户名
	public void checkUserName(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");

		UserService userService = new UserService();
		boolean isExit = userService.isExit(username);

		String json = "{\"isExit\":" + isExit + "}";
		response.getWriter().write(json);

	}

}