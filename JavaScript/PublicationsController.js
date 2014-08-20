myApp.controller('PublicationsController', function($scope, $http) {
    $scope.loading = false;
    //$scope.showErrorMessage=false;


    //private function not attached to the scope
    //console.log($scope.queryTitles);

    var deletePublication = function(publicationId) {
        var foundAt = $scope.publications.indexOf('publicationId', publicationId);
        if (publicationId >= 0) {
            $scope.publications.splice(foundAt, 1);
        }
    };

    Array.prototype.indexOf = function(property, value) {
        for (var i = 0; i < this.length; i++) {
            if (this[i][property] === value)
                return i;
        }
        return -1;
    };

    $scope.getPublications = function() {
         $scope.clearAlert();
        //console.log('titles before split!');
        //console.log($scope.queryTitles);

        var arrayOfLines = $('#queryTitles').val().split('\n').filter(Boolean);
        //console.log('titles after split!');
        
        //console.log(arrayOfLines);

        if (arrayOfLines === undefined || arrayOfLines === null || arrayOfLines.length === 0)
        {
            $scope.alertMessage ='Please Type in titles';
            return;
        }
        //console.log('titles after split!');
        $scope.alertMessage=null;
        var requestParameters = {
            titles: arrayOfLines,
            count: arrayOfLines.length
        };
        //console.log(arrayOfLines);
        //console.log(arrayOfLines.length);
        //console.log(requestParameters);

        $scope.loading = true; //clear loading

        $http.post('http://localhost:8080/HumanitarianScholarSearch/rs/publications/list', requestParameters)
                .success(function(response)
                {
                    //console.log(response);
                    //console.log('REST call');

                    $scope.publications = response;

                    //var resultTitles=$scope.publications.p;
                    ////console.log(typeof(resultTitles));
                    $scope.loading = false;
                }).error(function(response, status, headers, config)
        {
            //$scope.showErrorMessage=true;
            $scope.alertMessage = response;
            //console.log(response, status, config);
            $scope.loading = false;
           // $scope.showErrorMessage=false;

        });
    };

    //$scope.getPublications();


    $scope.clearAlert = function() {
        $scope.alertMessage = null;
    };
   

});
