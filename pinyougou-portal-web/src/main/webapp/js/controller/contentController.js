app.controller('contentController',function($scope,contentService){
	
	
	$scope.contentList=[];//����б�
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
				function(response){
					$scope.contentList[categoryId]=response;
				});
	}
	
	//���������ݲ�����
	$scope.search=function(){
		location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
	}
	
});