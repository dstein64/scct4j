'use strict';

var app = angular.module("app", [
  'ngRoute',
  'controllers',
  'directives',
  'filters'
]);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/manage', {
          templateUrl: 'partials/manage.html',
          controller: 'ManageController'
      }).
      when('/submit', {
          templateUrl: 'partials/submit.html',
          controller: 'SubmitController'
      }).
      when('/items/:item', {
          templateUrl: 'partials/item.html',
          controller: 'ItemController'
      }).
      otherwise({
          redirectTo: '/manage'
      });
}]);
