app.controller('searchController',function($scope,$location,searchService){
	
	//������������Ľṹ
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
	
	//����
	$scope.search=function(){
		$scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap = response;
				buildPageLabel();//������ҳ��
				
			}	
		);
	}
	
	
	//������ҳ��
	buildPageLabel=function(){
		$scope.pageLabel=[];
		var firstPage=1;//��ʼҳ��
		var lastPage=$scope.resultMap.totalPages//��ֹҳ��
		$scope.firstDot=true;//ǰ���е�
		$scope.lastDot=true;//�����е�
		
		if($scope.resultMap.totalPages>5){//���ҳ����������5
			if($scope.searchMap.pageNo<=3){//�����ǰҳ��С�ڵ���3����ʾǰ5ҳ				
				lastPage=5;
				$scope.firstDot=false;//ǰ��û��
			}else if($scope.searchMap.pageNo >= $scope.resultMap.totalPages-2){//��ʾ��5ҳ
				firstPage=$scope.resultMap.totalPages-4;
				$scope.lastDot=false;//����û��
			}else{//��ʾ�Ե�ǰҳΪ���ĵ�5ҳ
				firstPage = $scope.searchMap.pageNo-2;
				lastPage = $scope.searchMap.pageNo+2;
			}
			
		}else{
			$scope.firstDot=false;//ǰ���޵�
			$scope.lastDot=false;//�����޵�
		}
		//����ҳ��
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
	}
	
	//���������ı�searchMap��ֵ
	$scope.addSearchItem=function(key,value){
		if(key=='category' || key=='brand' || key=='price'){//����û�������Ƿ����Ʒ��
			$scope.searchMap[key]=value;
		}else{//�û�������ǹ��
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();//��ѯ
	}
	
	$scope.removeSearchItem=function(key){
		if(key=='category' || key=='brand' || key=='price'){//����û�������Ƿ����Ʒ��
			$scope.searchMap[key]="";
		}else{//�û�������ǹ��
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
	
	//��ҳ��ѯ
	$scope.queryBypage=function(pageNo){
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return;
		}
		$scope.searchMap.pageNo=pageNo;
		$scope.search();//��ѯ
	}
	//�жϵ�ǰҳ�Ƿ�Ϊ��һҳ
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	
	//�жϵ�ǰҳ�Ƿ�Ϊ���һҳ
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
	//����
	$scope.sortSearch=function(sortField,sort){
		$scope.searchMap.sortField=sortField;
		$scope.searchMap.sort=sort;
		$scope.search();//��ѯ
		
	}
	//�жϹؼ����Ƿ���Ʒ��
	$scope.keywordsIsBrand=function(){
		for(var i=0;i<$scope.resultMap.brandList.length;i++){
			if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
				return true;
			}
		}
		return false;
	}
	
	$scope.loadkeywords=function(){
		$scope.searchMap.keywords=$location.search()['keywords'];
		$scope.search();//��ѯ
	}
});