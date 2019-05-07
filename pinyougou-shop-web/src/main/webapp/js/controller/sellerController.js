 //控制层 
app.controller('sellerController' ,function($scope,$controller,sellerService,loginService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		sellerService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		sellerService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		sellerService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.sellerId!=null){//如果有ID
			serviceObject=sellerService.update( $scope.entity ); //修改  
		}else{
			serviceObject=sellerService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
		        	alert(response.message);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	$scope.passwordList={sellerId:"",oldPassword:"",newPassword1:"",newPassword2:""};
	//修改密码 
	$scope.updatePassword=function(){
		if($scope.passwordList.newPassword1 == ""){
			return alert("新密码不能为空！");
		}
		if($scope.passwordList.newPassword1!=$scope.passwordList.newPassword2){
			return alert("新密码不一致！");
		}
		var serviceObject;//服务层对象  				
		if($scope.entity.sellerId!=null){//如果有ID
			$scope.passwordList.sellerId=$scope.entity.sellerId;
			serviceObject=sellerService.updatePassword($scope.passwordList); //修改  
		}else{
			alert("用户不存在！");
		}				
		serviceObject.success(
			function(response){				
				if(response.success){
					//重新查询 
					$scope.passwordList={};
		        	alert(response.message);		        	
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//新增
	$scope.add=function(){						
		sellerService.add($scope.entity).success(
			function(response){
				if(response.success){
					//如果注册成功跳转到登录页 
		        	location.href="shoplogin.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		sellerService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		sellerService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	

	//获取当前用户名和商家信息
	$scope.findLoginUser = function(){
		loginService.loginName().success(
				function(response){
					$scope.loginName = response.loginName;
					//获取商家信息
					$scope.findOne($scope.loginName);
				}
		);		
	}
	
    
});	
