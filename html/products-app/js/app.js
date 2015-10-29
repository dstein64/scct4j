var app = angular.module("myApp", [
  'ngRoute',
  'controllers',
  'directives'
]);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
          templateUrl: 'partials/product-list.html',
          controller: 'MainController'
      }).
      otherwise({
          redirectTo: '/'
      });
}]);
