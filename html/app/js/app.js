var app = angular.module("myApp", [
  'ngRoute'
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
