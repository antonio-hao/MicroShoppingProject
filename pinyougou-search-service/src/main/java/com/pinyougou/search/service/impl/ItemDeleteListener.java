package com.pinyougou.search.service.impl;

import java.io.Serializable;
import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.pinyougou.search.service.ItemSearchService;

public class ItemDeleteListener implements MessageListener {
	@Autowired 
	private ItemSearchService itemSearchService;

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage =  (ObjectMessage) message;
		try {
			Long[] goodsIds = (Long[]) objectMessage.getObject();
			itemSearchService.deleteByGoodsIds(Arrays.asList(goodsIds));			
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
