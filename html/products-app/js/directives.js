'use strict';

var directives = angular.module('directives', []);

directives.directive('product', function() {
    return {
        restrict: 'E',
        templateUrl: 'partials/product.html'
    };
});
