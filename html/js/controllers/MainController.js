app.controller('MainController', ['$scope', function($scope) {
  $scope.title = 'My Top Sellers in Books';
  $scope.promo = "MyOwnString";
  $scope.products = [ 
  { 
    name: 'The Book of Trees', 
    price: 19,
    pubdate: new Date('2014', '03', '08'), 
    cover: 'https://codecademy5600959-8000.terminal.com/img/the-book-of-trees.jpg',
    likes: 0,
    dislikes: 0
  }, 
  { 
    name: 'Program or be Programmed', 
    price: 8,
    pubdate: new Date('2013', '08', '01'), 
    cover: 'https://codecademy5600959-8000.terminal.com/img/program-or-be-programmed.jpg',
    likes: 0,
    dislikes: 0
  },
    {
      name: 'The Alchemist',
      price: 12,
      pubdate: new Date('1988', '01', '01'),
      cover: 'https://upload.wikimedia.org/wikipedia/en/c/c4/TheAlchemist.jpg',
      likes: 0,
      dislikes: 0
    },
    {
      name: 'Harry Potter',
      price: 10,
      pubdate: new Date('1997', '01', '01'),
      cover: 'https://upload.wikimedia.org/wikipedia/en/6/6b/Harry_Potter_and_the_Philosopher%27s_Stone_Book_Cover.jpg',
      likes: 0,
      dislikes: 0
    }
];
  $scope.plusOne = function(index) {
    $scope.products[index].likes += 1;
  }
  
  $scope.minusOne = function(index) {
    $scope.products[index].dislikes += 1;
  }
  
}]);
