'use strict';

var directives = angular.module('directives', []);

directives.directive('navigation', function() {
    return {
        restrict: 'E',
        templateUrl: 'partials/navigation.html'
    };
});

directives.directive('twoLineDate', function() {
    return {
       restrict: 'E',
       template: '{{date | date : "M/d/yyyy"}}<br>{{date | date : "shortTime"}}',
       scope: {
          date: '@'
       }
    };
});

