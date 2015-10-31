'use strict';

var controllers = angular.module('controllers', []);

controllers.controller('SubmitController', function($scope, $http) {
    $scope.name = 'submit';
    $scope.labelCols = 2;
    $scope.valueCols = 6;
});

controllers.controller('ManageController', function($scope, $http) {
    $scope.name = 'manage';
    $scope.items = [];
    $scope.get = function() {
        $http.get('/items').then(function(response) {
            $scope.items = response.data;
        });
    };
    $scope.get();
    $scope.orderProp = 'modified';
    $scope.descProp = '-';
});

controllers.controller('ItemController', function($scope, $http, $routeParams, $location) {
    $scope.id = $routeParams.item;
    $scope.item = {};
    $scope.get = function() {
        $http.get('/item/' + $scope.id).then(function(response) {
            $scope.item = response.data;
        });
    };
    $scope.get();
    $scope.del = function() {
        var confirm = window.confirm("Are you sure you want to delete this?");
        if (confirm) {
            // TODO: real request
            $http.delete('/item/' + $scope.id, {}).then(function(response) {
                $location.path('/');
            }, function(response) {
                alert('Error Deleting');
            });
        }
    };
    $scope.update = function() {
        $location.path('/update/' + $scope.id);
    };
});

controllers.controller('UpdateController', function($scope, $http, $routeParams, $location) {
    $scope.id = $routeParams.item;
    $scope.item = {};
    $http.get('/item/' + $scope.id).then(function(response) {
        $scope.item = response.data;
    });
    $scope.save = function() {
        // TODO: Implement
        $http.post('items/' + $scope.id + '/update', {}).then(function(response) {
            $location.path('/item/' + $scope.id);
        }, function(response) {
            alert('Error Updating');
        });
    };
    $scope.cancel = function() {
        $location.path('/item/' + $scope.id);
    };
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
