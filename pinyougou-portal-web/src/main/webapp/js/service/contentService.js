app.service('contentService',function($http){
	
	//���ݹ�����Id��ѯ���
	this.findByCategoryId=function(categoryId){
		return $http.get('content/findByCategoryId.do?categoryId='+categoryId);
	}
});