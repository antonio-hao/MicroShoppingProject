//����ģ��
var app = angular.module('pinyougou',[]);

//���������
app.filter('trustHtml',['$sce',function($sce){
	
	return function(data){//��������Ǳ����˵�����
		return $sce.trustAsHtml(data);//���ص��ǹ��˺�����ݣ�����html��ת����
	}
	
}]);