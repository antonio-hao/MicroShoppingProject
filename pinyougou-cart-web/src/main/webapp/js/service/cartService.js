//���ﳵ�����
app.service('cartService',function($http){
	
	//���ﳵ�б�
	this.findCartList=function(){
		return $http.get('cart/findCartList.do');
	}
	
	//�����Ʒ�����ﳵ
	this.addGoodsToCartList=function(itemId,num){
		return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num=' + num);
	}
	
	//��ϼ�
	this.sum=function(cartList){
		var totalValue={totalNum:0,totalMoney:0};
		
		for(var i=0;i<cartList.length;i++){
			var cart=cartList[i];//���ﳵ����
			for(var j=0 ; j < cart.orderItemList.length;j++){
				var orderItem = cart.orderItemList[j];//���ﳵ��ϸ
				totalValue.totalNum+=orderItem.num;//�ۼ�����
				totalValue.totalMoney+=orderItem.totalFee;//�ۼӽ��
			}
		}
		return totalValue;
	}
	
	//��ȡ��ǰ��¼�˻����ջ���ַ
	this.findAddressList =function(){
		return $http.get('address/findListByLoginUser.do');
	}
	
	//�ύ����
	this.submitOrder=function(order){
		return $http.post('order/add.do',order);
	}
});