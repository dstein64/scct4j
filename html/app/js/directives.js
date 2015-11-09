'use strict';

var directives = angular.module('directives', []);

directives.directive('navigation', function() {
    return {
        restrict: 'E',
        templateUrl: 'app/partials/navigation.html'
    };
});

// makes <input type='range'> use numbers instead of strings
directives.directive('input', function() {
    return {
        restrict: 'E',
        require: '?ngModel',
        link: function(scope, element, attrs, ngModel) {
            if ('type' in attrs && attrs.type.toLowerCase() === 'range') {
                ngModel.$parsers.push(parseFloat);
            }
        }
    };
});

