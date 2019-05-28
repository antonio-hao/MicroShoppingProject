app.controller('baseController',function($scope){
	//��ҳ�ؼ����ò���
	$scope.paginationConf  = {
			currentPage: 1,//��ǰҳ
			totalItems: 10,//�ܼ�¼��
			itemsPerPage: 10,//ÿҳ��¼��
			perPageOptions: [10,20,30,40,50],//��ҳѡ��
			onChange: function(){
				$scope.reloadList();//���¼���
			}
	}
	
	//ˢ���б�
	$scope.reloadList = function(){
		$scope.selectIds=[];
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}
	
	$scope.selectIds=[];//�û���ѡ��ID����
	//�û���ѡ��ѡ��
	$scope.updateSelection=function($event,id){
		if($event.target.checked){
			$scope.selectIds.push(id);//push�򼯺����Ԫ��
		}else{
			var index = $scope.selectIds.indexOf(id);//����ֵ��λ��
			$scope.selectIds.splice(index,1);//�Ƴ���λ�ã�����
		}   			   			
	}
	
	$scope.jsonToString = function(jsonString,key){
		var json = JSON.parse(jsonString);
		var value = "";
		for(var i=0;i<json.length;i++){
			if(i>0){
				value += ",";
			}
			value += json[i][key];
		}
		return value;
	}
	
	//��List�����и���ĳKey�Ĳ�ѯ����
	$scope.searchObjectByKey=function(list,key,keyValue){
		for(var i=0;i<list.length;i++){
			if(list[i][key]==keyValue){
				return list[i];
			}
		}
		return null;
	}
});