//���ﳵ���Ʋ�
app.controller('cartController',function($scope,cartService){
	
	//��ѯ���ﳵ�б�
	$scope.findCartList=function(){
		cartService.findCartList().success(
				function(response){
					$scope.cartList=response;
					$scope.totalValue=cartService.sum($scope.cartList);//��ϼ���
				}
		);
	}
	
	//�����Ʒ�����ﳵ
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
				function(response){
					if(response.success){//����ɹ�
						$scope.findCartList();//ˢ���б�
						
					}else{
						alert(response.message);
					}
				}
		);
	}
	
	//��ȡ��ǰ�û��ĵ�ַ�б�
	$scope.findAddressList=function(){
		cartService.findAddressList().success(
			function(response){
				$scope.addressList=response;
				for(var i=0;i<$scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}
				}
			}	
		);
		
	}
	
	//ѡ���ַ
	$scope.selectAddress=function(address){
		$scope.address=address;
	}
	
	//�ж�ĳ��ַ�����ǲ��ǵ�ǰѡ��ĵ�ַ
	$scope.isSeletedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}
	}
	
	$scope.order={paymentType:1};//��������
	//ѡ��֧����ʽ
	$scope.selectPayType=function(type){
		$scope.order.paymentType=type;
	}
	
	//���涩��
	$scope.submitOrder=function(){
		$scope.order.receiverAreaName = $scope.address.address;//��ַ
		$scope.order.receiverMobile = $scope.address.mobile;//�ֻ�
		$scope.order.receiver =  $scope.address.contact;//��ϵ��
		
		cartService.submitOrder($scope.order).success(
				function(response){
					if(response.success){
						//ҳ���ת
						if($scope.order.paymentType=='1'){//�����΢��֧������ת��֧��ҳ��
							location.href="pay.html";
						}else{//������������ת����ʾҳ��
							location.href="paysuccess.html";
						}
						
					}else{
						alert(response.message);//Ҳ������ת����ʾҳ��
					}
				}
		);
	}
	
});