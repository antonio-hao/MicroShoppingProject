app.service('searchService',function($http){
	
	//ËÑË÷
	this.search=function(searchMap){
		return $http.post('itemsearch/search.do',searchMap);
	}
	
});