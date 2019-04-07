package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;

import entity.Result;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Reference(timeout=6000)
	private CartService cartService;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCardList(){
		
		//当前登录人帐号
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//从cookie中提取购物车
		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if(cartListString==null || cartListString.equals("")){
			cartListString="[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString,Cart.class);
		
		if(userName.equals("anonymousUser")){//未登录，从cookie中提取
			
			return cartList_cookie;
			
		}else{//已登录，从redis中提取
			//获取redis购物车
			List<Cart> cartList_redis = cartService.findCartListFromRedis(userName);
			
			//判断当本地购物车存在数据
			if(cartList_cookie.size()>0){
				//合并购物车
				List<Cart> cartList = cartService.mergeCartList(cartList_redis, cartList_cookie);
				//将合并后的购物车存入redis
				cartService.saveCartListToRedis(userName, cartList);
				util.CookieUtil.deleteCookie(request, response, "cartList");
				return cartList;
			}
				
			return cartList_redis;
		}
				
	}
	
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:9105",allowCredentials="true")//springmvc跨域配置
	public Result addGoodsToCartList(Long itemId,Integer num){
		
		//允许指定跨域访问，所有域用*,当此方法不需要操作cookie
		//response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
		//如果操作cookie，必须加上，允许使用cookie，上面的域不能写*,必须具体域名
		//response.setHeader("Access-Control-Allow-Credentials", "true");
		
		//当前登录人帐号
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			//从cookie中提取购物车
			List<Cart> cartList = findCardList();
			//调用服务方法操作购物车
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			
			if(userName.equals("anonymousUser")){//未登录，从cookie中提取
				//将新的购物车存入cookie
				String cartListString = JSON.toJSONString(cartList);
				util.CookieUtil.setCookie(request, response, "cartList", cartListString, 3600*24, "UTF-8");
				
			}else{//已登录，从redis中提取
				cartService.saveCartListToRedis(userName, cartList);
			}
			return new Result(true,"存入购物车成功");
			
		} catch (Exception e) {			
			e.printStackTrace();
			return new Result(false,"存入购物车失败");
		}
	}

}
