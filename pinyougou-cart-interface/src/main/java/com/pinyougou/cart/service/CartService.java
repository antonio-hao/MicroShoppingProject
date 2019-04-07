package com.pinyougou.cart.service;

import java.util.List;

import com.pinyougou.pojogroup.Cart;

/**
 * 购物车服务接口
 * @author hasee
 *
 */
public interface CartService {
	
	/**
	 * 添加商品到购物车列表
	 * @param list
	 * @param itemId
	 * @param num
	 * @return
	 */
	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
	
	/**
	 * 从redis中提取购物车
	 * @param username
	 * @return
	 */
	public List<Cart> findCartListFromRedis(String userName);
	

	 
	/**
	 * 将购物车存入redis
	 * @param userName
	 * @param cartList
	 */
	public void saveCartListToRedis(String userName,List<Cart> cartList);
	
	/**
	 * 合并购物车
	 * @return
	 */
	public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
