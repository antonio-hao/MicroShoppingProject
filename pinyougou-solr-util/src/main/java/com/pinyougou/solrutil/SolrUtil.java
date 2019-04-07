package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {
	
	@Autowired
	private TbItemMapper  itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importItemData(){		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//审核通过了才导入
		List<TbItem> itemList = itemMapper.selectByExample(example);		
		for(TbItem item : itemList){
			Map specMap = JSON.parseObject(item.getSpec(),Map.class);//从数据库中提取规格的字符串转为map
			item.setSpecMap(specMap);
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}
	
	public void deleteItemData(){
		Query query = new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}

	public static void main(String[] args){		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
		//solrUtil.deleteItemData();
	}
}
