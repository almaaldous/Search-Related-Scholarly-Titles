
myApp.controller('PublicationSearchByKeyword', function($scope, $http) {

    function onload() {
        var parser = document.createElement('a');
        parser.href = window.location;
        var libraryKeyword = '';
        var search = '';
        //console.log('Splitting the url:');
        search = parser.href.split('?');

        //console.log(search);
        if (search[1] === undefined || search[1] === null)
        {
            return;
        }
        libraryKeyword = search[1].replace(/%2B/g, ' ');
        //console.log(libraryKeyword);
        if (libraryKeyword !== undefined || libraryKeyword !== null)
        {
            getKeywordPublications(libraryKeyword);
        }
    }
    ;
    onload();
    $scope.getPublicationsByKeyword = function() {

        // //console.log('libraryKeyword:' + libraryKeyword);
        var keyword = stripEndQuotes($scope.keyword);
        if (keyword === undefined || keyword === null)
        {
            $scope.alertMessage ='Insert Valid Keyword, must match the library syntax';
            return;
        }
        //console.log('Keyword:' + keyword);
        $scope.loading = true; //clear loading

        $http.get('http://localhost:8080/HumanitarianScholarSearch/rs/publications/' + keyword)
                .success(function(response)
                {
                    //console.log(response);
                    //console.log('2nd REST call');
                    $scope.publicationsList = response;
                    //var resultTitles=$scope.publications.p;
                    ////console.log(typeof(resultTitles));
                     $scope.getKeywordPublicationsRelatedTitles();
                    //$scope.loading = false;
                }).error(function(response, status, headers, config)
        {
            $scope.alertMessage = response;
            //console.log(response, status, config);
            $scope.loading = false;
        });
    };
    function getKeywordPublications(libraryKeyword) {
       
        $scope.loading = true; //clear loading
        $scope.libraryKeyword = libraryKeyword;
        $http.get('http://localhost:8080/HumanitarianScholarSearch/rs/publications/' + libraryKeyword)
                .success(function(response)
                {
                    //console.log(response);
                    //console.log('3rd REST call');
                    $scope.publicationsList = response;
                    $scope.publicationsSize = publicationListSize($scope.publicationsList);
                    $scope.currentPublicationsSize=0;

                    //var resultTitles=$scope.publications.p;
                    console.log(typeof(resultTitles));
                    //$scope.loading = false;
                    $scope.showContinuePrompt = true;
                    $scope.getKeywordPublicationsRelatedTitles();
                }).error(function(response, status, headers, config)
        {
            $scope.alertMessage = response;
            //console.log(response, status, config);
            $scope.loading = false;
        });
    }
    ;
    function getTitlesPortion()
    {
        var index;
        var listSize = publicationListSize($scope.publicationsList);
        if (listSize >= 5)
        {
            index = 5;
        }
        else if (listSize < 5)
        {
            index = listSize;
        }
        $scope.currentPublicationsSize = $scope.currentPublicationsSize + index;

        var titles = new Array();
        var counter;
        for (counter = 0; counter < index; counter++)
        {
            titles.push($scope.publicationsList[counter]);
        }
        return  titles;
    }
    ;

    var deletePublication = function() {
        var counter = 0;
        for (counter = 0; counter < 5; counter++)
        {
            $scope.publicationsList.splice(counter);

        }
    };
    

    $scope.getKeywordPublicationsRelatedTitles= function()
    {
        // search 5 titles at a tim
        var arrayOfLines = getTitlesPortion();
        if (arrayOfLines === undefined || arrayOfLines === null)
            return;
         $scope.loading = true; //clear loading
        //title = title.substring(1, title.length() - 1);

        for (var i = 0; i < arrayOfLines.length; i++) 
        {
                    arrayOfLines[i]=stripEndQuotes(arrayOfLines[i]);
        }

        //console.log('Current Titles search' + arrayOfLines.toString());
        var requestParameters = {
            titles: arrayOfLines,
            count: arrayOfLines.length
        };
        //console.log(arrayOfLines.toString());
        //console.log(arrayOfLines.length);
        //console.log(requestParameters);

       

        $http.post('http://localhost:8080/HumanitarianScholarSearch/rs/publications/list', requestParameters)
                .success(function(response)
                {
                    //console.log('this is the REST response: '+response);
                    //console.log('REST call');

                    $scope.publications = response;
                    deletePublication();
                    $scope.loading = false;
                }).error(function(response, status, headers, config)
        {
            $scope.alertMessage = response;
            //console.log(response, status, config);
            $scope.loading = false;
        });
    }
    ;

    function publicationListSize(publicationsList) {
        var size = 0, key;
        for (key in publicationsList) {
            if (publicationsList.hasOwnProperty(key))
                size++;
        }
        return size;
    }
    ;
    function stripEndQuotes(s) {
        var t = s.length;
        if (s.charAt(0) === '"')
            s = s.substring(1, t--);
        if (s.charAt(--t) === '"')
            s = s.substring(0, t);
        return s;
    }
    $scope.getKeywords=function() {
        
         $scope.alertMessage = null; //clear alertMessage
        $http.get('http://localhost:8080/HumanitarianScholarSearch/rs/publications/keywords')
        .success(function(response) {
                    //console.log(response);

                    $scope.keywords =response;
                    
                    $scope.loading = false;
                }).error(function(response, status, headers, config) {
            $scope.alertMessage = response;
            //console.log(response, status, config);
            $scope.loading = false;
        });
        
    };
    $scope.getKeywords();


});
