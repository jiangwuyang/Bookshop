package com.youname.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.youname.domain.Cart;
import com.youname.domain.CartItem;
import com.youname.domain.Category;
import com.youname.domain.PageBean;
import com.youname.domain.Product;
import com.youname.domain.User;
import com.youname.service.CategoryService;
import com.youname.service.ProductService;
import com.youname.utils.CommonsUtils;
import com.youname.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

public class ProductServlet extends BaseServlet {

	/*
	 * public void doGet(HttpServletRequest request, HttpServletResponse response)
	 * throws ServletException, IOException { //获取请求那个方法的method String
	 * methodName=request.getParameter("method");
	 * 
	 * if ("categoryList".equals(methodName)) { categoryList(request,response);
	 * }else if ("index".equals(methodName)) { index(request,response); }else if
	 * ("productInfo".equals(methodName)) { productInfo(request,response); }else if
	 * ("productListByCid".equals(methodName)) { productListByCid(request,response);
	 * }
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * public void doPost(HttpServletRequest request, HttpServletResponse response)
	 * throws ServletException, IOException { doGet(request, response); }
	 */

	// 模块中功能同方法进行区分

	// 添加商品到购物车
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		// 判断用户是否已经登录 未登录下面代码不执行
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// 没有登录
			String tocart = "购物车跳过来的";
			request.getSession().setAttribute("tocart", tocart);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		} else {
			//目的：封装一个Cart对象，传递给service层
			Cart cart=null;

			
			//得到cartItemid
			String cartItemid=CommonsUtils.getUUID();
			//得到商品的pid
			String pid=request.getParameter("pid");
			//获得该商品的购买数量
			int count=Integer.parseInt(request.getParameter("count"));
			
			//通过得到的商品id获得该商品的所有信息
			ProductService service=new ProductService();
			Product product=service.findProductByPid(pid);
			//计算购物车项的小计
			double subtotal=product.getShop_price()*count;
			
			
			
			CartItem cartItem=new CartItem();
			cartItem.setCartitemid(cartItemid);
			cartItem.setProduct(product);
			cartItem.setCount(count);
			cartItem.setSubtotal(subtotal);
			cartItem.setCart(cart);
			List<Cart> listCart=service.selectCartByUserId(user.getUid());
			if (listCart.size()==0) {
				//得到购物车id
				//目的：封装一个Cart对象，传递给service层
			    cart=new Cart();
			}
				String cartid=CommonsUtils.getUUID();
				cart.setCartid(cartid);
				cart.setUser(user);
			    Map<String, CartItem> cartItems=service.selectCartItemByCartId(cartid);
					double newsubtotal = 0.0;
					if(cartItems.containsKey(pid)){
						//取出原有商品的数量
				    	cartItem = cartItems.get(pid);
						int oldCount = cartItem.getCount();
						oldCount+=count;
						cartItem.setCount(oldCount);
						cart.setCartItems(cartItems);
						//修改小计
						//原来该商品的小计
						double oldsubtotal = cartItem.getSubtotal();
						//新买的商品的小计
						newsubtotal = count*product.getShop_price();
						cartItem.setSubtotal(oldsubtotal+newsubtotal);
				} else{
			    	cart.setCartItems(cartItems);
				}
					double total = cart.getTotal()+newsubtotal;
					cart.setTotal(total);
			        service.addToCart(cart);
			        response.sendRedirect(request.getContextPath()+"/cart.jsp");
			
		}
		

	}

	// 显示商品类别的功能
	public void categoryList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CategoryService cService = new CategoryService();
		// 先从缓存中查询categoryList 如果有直接使用没有在从数据库差
		// 获得jedis对象连接redis数据库
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		if (categoryListJson == null) {
			System.out.println("redis没有数据,查询数据库");
			// 准备分类数据
			List<Category> categoryList = cService.findAllCategory();
			request.setAttribute("categoryList", categoryList);
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);
		}

		response.setContentType("Text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);

	}

	// 显示首页的功能
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		// response.sendRedirect(request.getContextPath()+"/index.jsp");
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	// 显示商品详细信息功能
	public void productInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得当前页、
		String currentPage = request.getParameter("currentPage");
		// 获得商品类别
		String cid = request.getParameter("cid");

		// 获得查询商品的pid
		String pid = request.getParameter("pid");
		request.getSession().setAttribute("pid", pid);
		ProductService service = new ProductService();
		Product product = service.findProductByPid(pid);
		CategoryService cService = new CategoryService();

		// 通过cid找类别
		Category category = cService.findAllCategoryByCid(cid);
		request.getSession().setAttribute("product", product);
		request.getSession().setAttribute("currentPage", currentPage);
		request.getSession().setAttribute("cid", cid);
		request.getSession().setAttribute("category", category);

		// 獲得客戶端攜帶的cookie----获得名字是pid的cookie
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 1-3-2本次访问商品pid是8---------->8-1-3-2
					// 1-3-2本次访问商品的pid是3---------->3-1-2
					// 1-3-2本次访问商品的pid是2---------->2-1-3
					// 将pids拆成一个数组
					String[] split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					if (list.contains(pid)) {
						// 包含当前查看商品的的pid
						list.remove(pid);
						list.addFirst(pid);
					} else {
						// 不包含当前查看的商品的pid，直接将该pid放到头上
						list.addFirst(pid);
					}
					// 将[3,1,2]转化成3-1-2字符串
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size() && i < 7; i++) {
						sb.append(list.get(i));
						sb.append("-");
					}
					// 去掉3-1-2-后的-
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}

		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);

		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}

	// 根据商品的类别获得商品的列表功能
	public void productListByCid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得cid
		String cid = request.getParameter("cid");
		String currentPageStr = request.getParameter("currentPage");
		if (currentPageStr == null)
			currentPageStr = "1";
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;

		ProductService service = new ProductService();
		PageBean pageBean = service.findProductListByCid(cid, currentPage, currentCount);
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
		// 定义一个记录历史商品信息的集合
		List<Product> historyProductList = new ArrayList<Product>();

		// 获得客户端携带的名字叫pids的cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for (String pid : split) {
						Product pro = service.findProductByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}

		// 将历史记录放到集合中
		request.setAttribute("historyProductList", historyProductList);

		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

}