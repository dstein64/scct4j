'use strict';

var controllers = angular.module('controllers', []);

// String#startsWith not in IE yet
var startsWith = function(str, start) {
    return str.indexOf(start) === 0;
};

// SubmitController and submit.html are used for submitting *and* updating
controllers.controller('SubmitController', function($scope, $http, $location, $routeParams) {
    $scope.updateFlag = startsWith($location.$$url, "/update/");
    
    if ($scope.updateFlag) {
        $scope.item = {};
        var id = $routeParams.item;
        $http.get('/item/' + id).then(function(response) {
            var data = response.data;
            $scope.item.name = data.name;
            $scope.item.id = data.id;
            $scope.item.created = data.created;
            $scope.item.modified = data.modified;
            $scope.item.existingFiles = data.files;
            for (var i = 0; i < $scope.item.existingFiles.length; i++) {
                $scope.item.existingFiles[i].keep = true;
            }
            $scope.item.priority = data.priority;
            $scope.item.description = data.description;
        }, function(response) {
            alert('Error Retrieving Item');
            $location.path('/item/' + id);
        });
    }
    
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
    
    // get form data for submission
    $scope.getFormData = function() {
        var fd = new FormData();
        
        fd.append('name', $scope.item.name);
        fd.append('priority', $scope.item.priority);
        fd.append('description', $scope.item.description);
        
        for (var i = 0; i < $scope.files.length; i++) {
            fd.append('files[]', $scope.files[i]);
        }
        
        if ($scope.updateFlag) {
            for (var i = 0; i < $scope.item.existingFiles.length; i++) {
                var f = $scope.item.existingFiles[i];
                if (!f.keep) {
                    fd.append('removefiles[]', f.id);
                }
            }
        }
        
        return fd;
    }
    
    var formHasChanges = function() {
        var hasChanges = !document.getElementById('form').classList.contains('ng-pristine');
        hasChanges = hasChanges || ($scope.files.length > 0);
        return hasChanges;
    }
    
    var offLocationChange = $scope.$on('$locationChangeStart', function(event, next, current) {
        if (formHasChanges() && !confirm("Your changes will be lost.\nContinue?")) {
            event.preventDefault();
        }
    });
    
    // angular also has form validation functionality, but using what's built-in to javascript
    var forceChangePage = function(path) {
        offLocationChange();
        $location.path(path);
    };
    
    $scope.submit = function() {
        if (!document.getElementById('form').checkValidity())
            return false;
        
        var totalSize = 0;
        var maxSize = 100 * 1024 * 1024; // 100 MiB
        for (var i = 0; i < $scope.files.length; i++) {
            totalSize += $scope.files[i].size;
        }
        if (totalSize > maxSize) {
            alert("A submission cannot exceed 100MiB.");
            return false;
        }
        
        var fd = $scope.getFormData();
        
        var method = $http.post;
        var endpoint = '/item/';
        var successRedirect = '/';
        var failureMessage = 'Error Submitting';
        
        if ($scope.updateFlag) {
            var id = $routeParams.item;
            method = $http.put;
            endpoint = '/item/' + id;
            successRedirect = '/item/' + id;
            failureMessage = 'Error Saving';
        }
        
        method(endpoint, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(response) {
            forceChangePage(successRedirect);
        }, function(response) {
            alert(failureMessage);
        });
    };
});

controllers.controller('ManageController', function($scope, $http) {
    $scope.items = [];
    $scope.get = function() {
        $http.get('/items').then(function(response) {
            $scope.items = response.data;
        }, function(response) {
            alert('Error Retrieving Items');
            // Unlike other controllers, no redirect in this scenario
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
        }, function(response) {
            alert('Error Retrieving Item');
            $location.path('/');
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

controllers.controller('NavigationController', function($scope, $location) {
    $scope.isActive = function(val) {
        return ("/" + val) === $location.$$url;
    };
});
