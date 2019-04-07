//秒杀控制层
app.controller('seckillGoodsController',function($scope,$location,$interval,seckillGoodsService){
	
	//查询秒杀商品列表
	$scope.findList=function(){
		seckillGoodsService.findList().success(
				function(response){
					$scope.list=response;
				}
		);
	}
	
	//查询商品
	$scope.findOne=function(){
		//接受参数
		var id = $location.search()['id'];
		seckillGoodsService.findOne(id).success(
				function(response){
					$scope.entity=response;
					
					//倒计时开始
					//获取从结束时间到当前时间的秒数
					allSecond=Math.floor((new Date($scope.entity.endTime).getTime()-new Date().getTime())/1000);
					time = $interval(
						function(){
							allSecond=allSecond-1;
							$scope.timeString=convertTimeString(allSecond);
							if(allSecond<=0){
								$interval.cancel(time);
							}
							
					},1000);
				}
				
		);
	}
	
	//转换秒为天小时分钟秒格式：XX天 10：22：33
	convertTimeString=function(allSecond){
		var days=Math.floor(allSecond/(60*60*24));//天数
		var hours=Math.floor((allSecond-days*60*60*24)/(60*60));//小时数
		var minutes = Math.floor((allSecond-days*60*60*24-hours*60*60)/60);//分钟
		var second = allSecond-days*60*60*24-hours*60*60-minutes*60;//秒数
		var timeString="";
		if(days>0){
			timeString+=days+"天";
		}
		if(hours<10){
			hours="0"+hours;
		}
		if(minutes<10){
			minutes="0"+minutes;
		}
		if(second<10){
			second="0"+second;
		}
		return timeString+hours+":"+minutes+":"+second;
	}
	
	//提交订单
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder($scope.entity.id).success(
				function(response){
					if(response.success){//如果下单成功
						alert("抢购成功，请在5分钟之内完成支付");
						location.href="pay.html";//跳转到支付页面
						
					}else{
						alert(response.message);
					}
				}
		);
	}
});