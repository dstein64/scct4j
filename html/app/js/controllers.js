'use strict';

var controllers = angular.module('controllers', []);

controllers.controller('SubmitController', function($scope, $http) {
    $scope.name = 'submit';
});

controllers.controller('ManageController', function($scope, $http, $location) {
    $scope.name = 'manage';
    $scope.items = [];
    $http.get('items.json').success(function(data) {
        $scope.items = data;
    });
    $scope.orderProp = '-modified';
});

controllers.controller('ItemController', function($scope, $routeParams) {
    $scope.id = $routeParams.item;
    
});

controllers.controller('NavigationController', function($scope, $route) {
    $scope.isActive = function(val) {
        var _scope = $route.current.scope;
        if (_scope && ('name' in _scope)) {
            return val === $route.current.scope.name;
        } else {
            return false;
        }
    };
});
