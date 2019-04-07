app.controller('brandController',function($scope,$controller,$http,brandService){
			
			//�������̳�
			$controller('baseController',{$scope:$scope});
				
    		//��ѯƷ���б�
    		$scope.findAll=function(){
    			brandService.findAll().success(
    				function(response){
    					$scope.list = response;
    				}		
    			);
    		}
    		

    		//��ҳ
    		$scope.findPage = function(page,size){
    			brandService.findPage(page,size).success(
    				function(response){
    					$scope.list = response.rows;//��ʾ��ǰҳ����
    					$scope.paginationConf.totalItems = response.total;//�����ܼ�¼��
    				}		
    			)
    		}
    		//����&�޸�
    		$scope.save=function(){
    			var object = null;
    			if($scope.entity.id != null){
    				object = brandService.update($scope.entity);
    			}else{
    				object = brandService.add($scope.entity);
    			}
    			
    			object.success(
    				function(response){
    					if(response.success){
    						$scope.reloadList();//ˢ��
    					}else{
    						alert(response.message);
    					}
    				}		
    			)
    		}
    		
    		//��ѯʵ��
    		$scope.findOne=function(id){
    			brandService.findOne(id).success(
    				function(response){
    					$scope.entity=response;
    				}		
    			)
    		}
    		
    		
    		//ɾ��
    		$scope.dele = function(){
    			brandService.dele($scope.selectIds).success(
    				function(response){
    					if(response.success){
    						$scope.reloadList();//ˢ��
    					}else{
    						alert(response.message);
    					}
    				})
    		}
    		
    		$scope.searchEntity={};
    		//������ѯ
    		$scope.search=function(page,size){
    			brandService.search(page,size,$scope.searchEntity).success(
    				function(response){
    					$scope.list = response.rows;//��ʾ��ǰҳ����
    					$scope.paginationConf.totalItems = response.total;//�����ܼ�¼��
    				}		
    			)
    		}
    		
    	});