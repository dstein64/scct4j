app.controller('MainController', function($scope, $http) {
    $http.get('products.json').success(function(data) {
        $scope.products = data;
    });


    $scope.title = 'My Top Sellers in Books';
    $scope.promo = "MyOwnString";



    $scope.plusOne = function(index) {
        $scope.products[index].likes += 1;
    }

    $scope.minusOne = function(index) {
        $scope.products[index].dislikes += 1;
    }
});

