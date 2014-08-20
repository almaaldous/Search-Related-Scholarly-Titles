var myApp = angular.module('myApp', ['ngRoute']);
myApp.config(function($routeProvider) {
    $routeProvider
            .when('/',
                    {
                        templateUrl: '/HumanitarianScholarSearch/partials/searchPublication.html',

                    })
            .when('/publications',
                    {
                        templateUrl: '/HumanitarianScholarSearch/partials/searchPublication.html',
                        controller: 'PublicationsController'
                    })
            .when('/publications/:keyword?',
                    {
                        templateUrl: '/HumanitarianScholarSearch/partials/searchByKeyword.html',
                        controller: 'PublicationSearchByKeyword'
                    })
            .otherwise({redirectTo: '/'});
});


