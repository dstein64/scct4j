'use strict';

var controllers = angular.module('controllers', []);

controllers.controller('SubmitController', function($scope, $http, $location) {
    $scope.files = [];
    $scope.change = function(file) {
        if (file.value) {
            $scope.files.push(file.files[0]);
            $scope.$apply();   
        }
    };
    $scope.labelCols = 2;
    $scope.valueCols = 6;
    
    $scope.filesOffset = function() {
        if ($scope.files.length <= 0)
            return 'col-md-offset-' + $scope.labelCols;
        else
            return '';
    };
    
    $scope.submit = function() {
        var fd = new FormData();
        
        fd.append('name', $scope.item.name);
        fd.append('priority', $scope.item.priority);
        fd.append('description', $scope.item.description);
        
        for (var i = 0; i < $scope.files.length; i++) {
            fd.append('files[]', $scope.files[i]);
        }
        
        $http.post('/item/', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(response) {
            $location.path('/');
        }, function(response) {
            alert('Error Submitting');
        });
    };
    
});

controllers.controller('ManageController', function($scope, $http) {
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

controllers.controller('NavigationController', function($scope, $location) {
    $scope.isActive = function(val) {
        return ("/" + val) === $location.$$url;
    };
});
