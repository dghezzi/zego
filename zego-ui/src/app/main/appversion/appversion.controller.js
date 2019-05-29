(function ()
{
    'use strict';

    angular
        .module('app.version.list',[
          'ngTable'
        ])
        .controller('AppversionListController', AppversionListController)
        .controller('AppversionDialogController', AppversionDialogController);



    function AppversionDialogController($scope,$mdDialog,api,item){
      $scope.titoloDialog = "Aggiungi versione app";

      $scope.pattern = '[0-9]';
      if( item ){
        $scope.appversion = angular.copy(item);
        if( $scope.appversion.deprecatedate )
          $scope.appversion.deprecatedate = new Date( $scope.appversion.deprecatedate *1000 );
        $scope.titoloDialog = "Modifica versione app";
      }



      /*$scope.$watch("appversion.vers",function(n){

        if(n==undefined){
          n = "0.0.0";
        }
        var e = n.split(".");

        var r=[];
        for (var i = 0; i < 3; i++) {
          if( e[i] !== undefined  ){
            if(parseInt(e[i]) > 99)
              e[i]=99;
              r[i]=e[i];

          }else{
            r[i]=0;
          }
        }

        n = r.join(".");
        console.log(n);
        $scope.appversion.vers = n;

      });
      */

      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){
        var reg = new RegExp("^[0-9]{1,2}\.[0-9]{1,2}\.[0-9]{1,2}$");

        if( !reg.test( $scope.appversion.vers ) ){
            alert("La versione deve essere in formato x.x.x");
            return;
        }


        var s = angular.copy( $scope.appversion );
        if($scope.appversion.deprecatedate != undefined){
            s.deprecatedate = Math.round($scope.appversion.deprecatedate.getTime() /1000);
        }


        if( s.id !== undefined ){

          api.appversion.put({id:s.id},s,
            function(r){
              $scope.answer(r);
            })

        }else{
          api.appversion.post(s,
            function(r){
              $scope.answer(r);
            })
        }


      }
    }


    /** @ngInject */
    function AppversionListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



      function loadappversion(){
        api.appversion.getAll({},
          function(result){
            vm.appversionList = result;


            $scope.tabella = new NgTableParams({}, { dataset : vm.appversionList});

            console.log(result);
          },
          function(error){
            console.log(error);
          }
        );
      }
        // Data

        // Methods

        //////////

        loadappversion();

        $scope.aggiornaElemento = function(appversion){

          api.appversion.put({id : appversion.id},appversion);
        }
        $scope.modifica = function(ev,appversion){
              $mdDialog.show({
                controller: AppversionDialogController,
                templateUrl: 'app/main/appversion/dialog-form.html',
                parent: angular.element(document.body),
                locals : {
                    item : appversion
                },
                targetEvent: ev,
                clickOutsideToClose:false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
              })
              .then(function(answer) {
                 loadappversion();
              }, function() {

              });
        }

        $scope.showAdvanced = function(ev) {
               $mdDialog.show({
                 controller: AppversionDialogController,
                 templateUrl: 'app/main/appversion/dialog-form.html',
                 parent: angular.element(document.body),
                 targetEvent: ev,
                 locals : {
                     item : null
                 },
                 clickOutsideToClose:true,
                 fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
               })
             .then(function(answer) {
                loadappversion();
             }, function() {

             });
       };


    }



})();
