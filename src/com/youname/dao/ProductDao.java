package com.youname.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.youname.domain.Cart;
import com.youname.domain.CartItem;
import com.youname.domain.Product;
import com.youname.utils.DataSourceUtils;

public class ProductDao {
	//获得热门商品
	public List<Product> findHotProductList() throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where is_hot=? limit?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class),1,0,9);
	}
	//获得热门商品
		public List<Product> findNewProductList() throws SQLException {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select * from product order by pdate desc limit?,?";
			return runner.query(sql, new BeanListHandler<Product>(Product.class),0,9);
		}
		public int getCount(String cid) throws SQLException {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select count(*) from product where cid=?";
			Long query=(Long) runner.query(sql, new ScalarHandler(),cid);
			return query.intValue();
		}
		public List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select * from product where cid=? limit ?,?";
			List<Product> list=runner.query(sql, new BeanListHandler<Product>(Product.class),cid,index,currentCount);
			return list;
		}
		public Product findProductByPid(String pid) throws SQLException {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select * from product where pid=?";
			return runner.query(sql, new BeanHandler<Product>(Product.class),pid);
		}
		public List<Cart> selectCartByUserId(String uid) throws SQLException {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select * from cart where uid=?";
			List<Cart> listCart=runner.query(sql, new BeanListHandler<Cart>(Cart.class),uid);
			return listCart;
		}
		public Map<String, CartItem> selectCartItemByCartId(String cartid) throws SQLException {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select * from cartitem where cartid=?";
			Map<String, CartItem> CartItems=(Map<String, CartItem>) runner.query(sql, new BeanListHandler<CartItem>(CartItem.class),cartid);
			return CartItems;
		}
		public void addCart(Cart  cart) throws SQLException {
			QueryRunner runner = new QueryRunner();
			String sql = "insert into cart values(?,?,?)";
			Connection conn = DataSourceUtils.getConnection();
			runner.update(conn,sql, cart.getCartid(),cart.getTotal(),cart.getUser().getUid());
		}
		public void addCartItem(Cart cart) throws SQLException {
			QueryRunner runner = new QueryRunner();
			String sql = "insert into cartitem values(?,?,?,?,?)";
			Connection conn = DataSourceUtils.getConnection();
			List<CartItem> cartItem = (List<CartItem>) cart.getCartItems();
			for(CartItem item : cartItem){
				runner.update(conn,sql,item.getCartitemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getCart().getCartid());
			}
		}
		
}
