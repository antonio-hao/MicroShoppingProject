app.service('uploadService',function($http){
	
	//�ϴ��ļ�
	this.uploadFile = function(){
		var formdata = new FormData();
		formdata.append('file',file.files[0]);//file �ļ��ϴ����name
		return $http({
			url:'../upload.do',
			method:'post',
			data:formdata,
			headers:{'Content-Type':undefined},
			transformRequest:angular.identity
		});
	}
})