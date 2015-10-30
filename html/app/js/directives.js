'use strict';

var directives = angular.module('directives', []);

directives.directive('navigation', function() {
    return {
        restrict: 'E',
        templateUrl: 'partials/navigation.html'
    };
});


