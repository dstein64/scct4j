'use strict';

angular.module('filters', []).filter('mydate', function($filter) {
    return function(input) {
        // use a non-breaking space in the between numeric time and AM/PM
        return $filter('date')(input, "M/d/yyyy h:mm\xA0a");
    };
});
