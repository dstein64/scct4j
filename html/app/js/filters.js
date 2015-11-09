'use strict';

var filtes = angular.module('filters', []);

// 'short' date with a non-breaking space in the between numeric time and AM/PM
filtes.filter('mydate', function($filter) {
    return function(input) {
        return $filter('date')(input, "M/d/yyyy h:mm\xA0a");
    };
});

// non-breaking date. that is, all spaces replaced with non-breaking spaces
filtes.filter('nbdate', function($filter) {
    return function(input, format) {
        var d = $filter('date')(input, format);
        if (d) d = d.split(' ').join('\xA0');
        return d;
    };
});

filtes.filter('filesize', function () {
    var units = ['bytes', 'KiB', 'MiB', 'GiB', 'TiB', 'PiB', 'EiB', 'ZiB', 'YiB'];

    return function( bytes, precision ) {
        if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) {
            return '?';
        }

        var unit = 0;
        for (var i = 0; i < units.length-1; i++) {
            if (bytes >= 1024) {
                bytes /= 1024;
                unit++;
            } else {
                break;
            }
        }

        return bytes.toFixed( + precision ) + '\xA0' + units[unit];
    };
});
