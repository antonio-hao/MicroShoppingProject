//��ɱ�����
app.service('seckillGoodsService',function($http){
	
	//��ȡ����˵���Ч��ɱ��Ʒ
	this.findList=function(){
		return $http.get('seckillGoods/findList.do');
	}
	
	//��ѯ��Ʒ
	this.findOne=function(id){
		return $http.get('seckillGoods/findOneFromRedis.do?id='+id);
	}
	
	//�ύ����
	this.submitOrder=function(seckillId){
		return $http.get('seckillOrder/submitOrder.do?seckillId='+seckillId);
	}
});