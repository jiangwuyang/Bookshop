package com.youname.domain;

public class CartItem {
	private String cartitemid;//该购物车项的id
	private int count;//购物车项内商品的购买数量
	private double subtotal;//购物车项小计
	private Product product;//购物车项内部的商品
	private Cart cart;//该购物车项属于哪个购物车
	public String getCartitemid() {
		return cartitemid;
	}
	public void setCartitemid(String cartitemid) {
		this.cartitemid = cartitemid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	
}
