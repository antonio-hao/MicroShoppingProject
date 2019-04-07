//֧�����Ʋ�
app.controller('payController',function($scope,$location,payService){
	
	$scope.createNative=function(){
		payService.createNative().success(
				function(response){
					//��ʾ�����źͽ��
					$scope.money=(response.total_fee/100).toFixed(2);
					$scope.out_trade_no=response.out_trade_no;
					
					//���ɶ�ά��
					var qr =new QRious({
						element:document.getElementById('qrious'),
						size:250,
						value:response.code_url,
						level:'H'
					});
					
					queryPayStatus();//���ò�ѯ
				}
		);
	}
	
	//��ѯ֧��״̬
	queryPayStatus=function(){
		payService.queryPayStatus($scope.out_trade_no).success(
				function(response){
					if(response.success){						
						location.href="paysuccess.html#?money="+$scope.money;
					}else{
						if(response.message=="��ά�볬ʱ"){
							//$scope.createNative();//�������ɶ�ά��
						}else{
							location.href="payfail.html#?message="+response.message;
						}
						
					}
				}
		);
	}
	
	$scope.getMoney=function(){
		return $location.search()['money'];
	}
	
	$scope.getMessage=function(){
		return $location.search()['message'];
	}
});