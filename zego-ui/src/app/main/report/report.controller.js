(function ()
{
    'use strict';

    angular
        .module('app.report')
        .controller('ReportController', ReportController);



    function ReportController($scope,$mdDialog,api){

      $scope.da = new Date();
      $scope.a = new Date();

      $scope.daf = Math.round($scope.da.getTime() / 1000 );
      $scope.af = Math.round($scope.a.getTime() / 1000 );

      $scope.apibase = api.baseUrl;


      $scope.$watch("da",function(){
        if($scope.da != undefined){
          $scope.da.setHours(0, 0, 0, 0);
          $scope.a.setHours(23, 59, 59, 0);
          $scope.daf = Math.round($scope.da.getTime() / 1000 );
          $scope.af = Math.round($scope.a.getTime() / 1000 );
        }
      });

      $scope.$watch("a",function(){
        if($scope.a != undefined){
          $scope.da.setHours(0, 0, 0, 0);
          $scope.a.setHours(23, 59, 59, 0);
          $scope.daf = Math.round($scope.da.getTime() / 1000 );
          $scope.af = Math.round($scope.a.getTime() / 1000 );
        }
      });





       api.zone.getAll().$promise.then(function(r){
                   $scope.zone=r;
                 });


    }


})();
