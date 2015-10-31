'use strict';

var controllers = angular.module('controllers', []);

controllers.controller('SubmitController', function($scope, $http) {
    $scope.name = 'submit';
    $scope.files = [];
    $scope.counter = 0;
    $scope.change = function(file) {
        console.log($scope.f);
        $scope.files.push(file.files[0]);
        $scope.$apply();
        $scope.f = "";
    };
    $scope.labelCols = 2;
    $scope.valueCols = 6;
    
    $scope.submit = function() {
        var fd = new FormData();
        for (var i = 0; i < $scope.files.length; i++) {
            fd.append('file', $scope.files[i]);
        }
        
        
        $http.post('/item/0', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(response) {
            console.log('hello');
        });
    };
    
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
