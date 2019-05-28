package com.pinyougou.shop.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			//获取商家Id
			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			goods.getGoods().setSellerId(sellerId);//设置商家Id
			goods.getGoods().setIsMarketable("0");//默认为未上架状态
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		//获取商家Id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//判断商品是否为该商家的商品
		Goods goods2 = goodsService.findOne(goods.getGoods().getId());
		if(!goods2.getGoods().getSellerId().equals(sellerId) || !goods.getGoods().getSellerId().equals(sellerId)){
			return new Result(false, "非法操作");
		}
		try {
			goodsService.update(goods);
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
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
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
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		//获取商家ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);		
	}
	
	@RequestMapping("/updateMarketable")
	public Result updateMarketable(Long[] ids,String marketable){
	    
	    List<Long> idList=new ArrayList<Long>();
        //审核通过（1）,判断是否上架（1）、未上架（0）     
            for(Long id : ids){
                Goods good = goodsService.findOne(id);
                String auditStatus = good.getGoods().getAuditStatus();
                String marke = good.getGoods().getIsMarketable();
                if("1".equals(auditStatus) && !marketable.equals(marke)){
                    idList.add(id);
                }
            }
            
            if(idList.isEmpty()&&idList.size()==0){
                return new Result(false,"只有审核通过的商品才可以上下架！");
            }
            
            //下架上架设置SKU状态，solr，静态模板
            //略
	    
		try {
			goodsService.updateMarketable(idList.toArray(new Long[idList.size()]), marketable);
			return new Result(true,"成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"失败");
		}
	}
	
	@RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status){
	    
	    List<Long> idList=new ArrayList<Long>();
	    //判断是否是提交审核，过滤审核通过（1）、已提交审核状态（4）
	    if("4".equals(status)){
	        for(Long id : ids){
	            Goods good = goodsService.findOne(id);
	            String auditStatus = good.getGoods().getAuditStatus();
	            if(!"1".equals(auditStatus) && !"4".equals(auditStatus)){
	                idList.add(id);
	            }
	        }
	        if(idList.isEmpty()&&idList.size()==0){
	               return new Result(false,"审核通过和已提交审核的不能再次提交！");
	        }
	        ids=idList.toArray(new Long[idList.size()]);
	    }else if("3".equals(status)){
	        for(Long id : ids){
                Goods good = goodsService.findOne(id);
                String auditStatus = good.getGoods().getAuditStatus();
                String marke = good.getGoods().getIsMarketable();
                if("1".equals(auditStatus) && "0".equals(marke)){
                    idList.add(id);
                }
            }
	        if(idList.isEmpty()&&idList.size()==0){
                return new Result(false,"审核通过并且未上架的商品才能关闭！");
         }
         ids=idList.toArray(new Long[idList.size()]);
	    }
	    //关闭商品更新sku状态，solr，静态模板
	    //略
	    
        try {
            goodsService.updateStatus(ids, status);          
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }
	
	
}
