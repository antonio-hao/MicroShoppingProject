package com.pinyougou.shop.controller;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return sellerService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeller seller){
		//密码加密
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(seller.getPassword());//加密
		seller.setPassword(password);
		try {
			sellerService.add(seller);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
	
	/**
	 * 更新用户密码
	 * @param seller
	 * @return
	 */
	@RequestMapping("/updatePassword")
	public Result updatePassword(@RequestBody HashMap<String,String> passwordList){	    
	  
	    TbSeller seller = sellerService.findOne(passwordList.get("sellerId"));
	  //密码加密
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String oldPassword = passwordList.get("oldPassword");
	    String newPassword1 = passwordList.get("newPassword1");
	    String newPassword2 = passwordList.get("newPassword2");
	    String newPassword="";
	   if(newPassword1.equals(newPassword2)){
	       newPassword= passwordEncoder.encode(newPassword1);
	   }else{
	       return new Result(false, "新密码不一致");  
	   }
	   if(passwordEncoder.matches(oldPassword, seller.getPassword())){
	       seller.setPassword(newPassword);
	   }else{
	       return new Result(false, "原密码错误，修改失败。");
	   }

       try {
           sellerService.update(seller);
           return new Result(true, "修改成功");
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false, "修改失败");
       }
	}
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbSeller findOne(String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(String [] ids){
		try {
			sellerService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeller seller, int page, int rows  ){
		return sellerService.findPage(seller, page, rows);		
	}
	
	
	
}
