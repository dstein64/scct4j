'use strict';

var app = angular.module("app", [
  'ngRoute',
  'controllers',
  'directives',
  'filters'
]);

app.config(function($routeProvider) {
    $routeProvider.
      when('/manage', {
          templateUrl: 'app/partials/manage.html',
          controller: 'ManageController'
      }).
      when('/submit', {
          templateUrl: 'app/partials/submit.html',
          controller: 'SubmitController'
      }).
      when('/item/:item', {
          templateUrl: 'app/partials/item.html',
          controller: 'ItemController'
      }).
      when('/update/:item', {
          templateUrl: 'app/partials/submit.html',
          controller: 'SubmitController'
      }).
      when ('/', {
          redirectTo: '/manage'
      }).
      otherwise({
          redirectTo: '/manage'
      });
});
