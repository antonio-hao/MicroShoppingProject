//秒杀服务层
app.service('seckillGoodsService',function($http){
	
	//读取已审核的有效秒杀商品
	this.findList=function(){
		return $http.get('seckillGoods/findList.do');
	}
	
	//查询商品
	this.findOne=function(id){
		return $http.get('seckillGoods/findOneFromRedis.do?id='+id);
	}
	
	//提交订单
	this.submitOrder=function(seckillId){
		return $http.get('seckillOrder/submitOrder.do?seckillId='+seckillId);
	}
});