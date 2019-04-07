package com.pinyougou.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		
		//1.根据sku的ID查询商品明细SKU对象
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if(item == null){
			throw  new RuntimeException("商品不存在："+itemId);
		}
		
		if(!item.getStatus().equals("1")){
			throw  new RuntimeException("商品状态不合法："+itemId);
		}
		
		//2.根据SKU对象得到商家ID
		String sellerId = item.getSellerId();
		
		//3.根据商家ID在购物车列表中查询购物车对象
		Cart cart = searchCartBySellerId(cartList, sellerId);
		
		if(cart == null){//4.如果购物车列表中不存在该商家的购物车对象
			
			//4.1创建一个新的购物车对象
			cart = new Cart();
			cart.setSellerId(sellerId);//商家ID
			cart.setSellerName(item.getSeller());//商家名称
			List<TbOrderItem> orderItemList = new ArrayList<TbOrderItem>();//创建购物车明细列表
			TbOrderItem orderItem = createOrderItem(item,num);
			orderItemList.add(orderItem);
			cart.setOrderItemList(orderItemList);
			//4.2将新的购物车对象添加到购物车列表中
			cartList.add(cart);
		}else{//5.如果购物车列表中存在该商家的购物车对象
			//判断该商品是否在该购物车对象的明细列表中存在
			TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),itemId);
			if(orderItem==null){
				//5.1如果不存在，创建新的购物车明细对象，并添加到购物车的对象中
				orderItem=createOrderItem(item,num);
				cart.getOrderItemList().add(orderItem);
			}else{
				//5.2如果存在，在原有的数量上添加数量，并更新金额
				orderItem.setNum(orderItem.getNum()+num);//更改数量
				//金额
				orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
				//当明细的数量小于等于0，移除此明细
				if(orderItem.getNum()<=0){
					cart.getOrderItemList().remove(orderItem);
				}
				//当购物车的明细数量为0，在购物车列表中移除此购物车对象
				if(cart.getOrderItemList().size()==0){
					cartList.remove(cart);
				}
			}
				
		}
		return cartList;
	}
	
	/**
	 * 根据商家ID在购物车列表中查询购物车对象
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
		for(Cart cart:cartList){
			if(cart.getSellerId().equals(sellerId)){
				return cart;
			}
		}
		return null;
	}
	
	/**
	 * 根据SKUID在购物车明细列表中查询购物车明细对象
	 * @param orderItemList
	 * @param itemId
	 * @return
	 */
	private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId){
		for(TbOrderItem orderItem : orderItemList){
			if(orderItem.getItemId().longValue() == itemId.longValue()){
				return orderItem;
			}
		}
		
		return null;
	}
	
	
	/**
	 * 创建购物车明细对象
	 * @param item
	 * @param num
	 * @return
	 */
	private TbOrderItem createOrderItem(TbItem item ,Integer num){
		//创建购物车明细
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
		return orderItem;
	}

	@Override
	public List<Cart> findCartListFromRedis(String userName) {
		//System.out.println("从redis中提取购物车："+userName);
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(userName);
		if(cartList==null){
			cartList=new ArrayList<Cart>();
		}
		return cartList;
	}

	@Override
	public void saveCartListToRedis(String userName, List<Cart> cartList) {
		//System.out.println("向redis中存入购物车："+userName);
		redisTemplate.boundHashOps("cartList").put(userName, cartList);
		
	}

	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		for(Cart cart : cartList2){
			for(TbOrderItem orderItem : cart.getOrderItemList()){
				cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
			}
		}
		return cartList1;
	}

}