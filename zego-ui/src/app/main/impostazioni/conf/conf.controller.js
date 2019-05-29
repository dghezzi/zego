(function ()
{
    'use strict';

    angular
        .module('app.conf.list',[])
        .controller('ConfListController', ConfListController)
        .controller('ConfDialogController', ConfDialogController);



    function ConfDialogController($scope,$mdDialog,api,item){
      $scope.titoloDialog = "Aggiungi configuzione";
      if( item ){
        $scope.conf = item;
        $scope.titoloDialog = "Modifica configuzione";
      }

      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){
        if(!$scope.conf || !$scope.conf.k || !$scope.conf.val || !$scope.conf.descr  ){
          $scope.messaggioErrore = "Tutti i campi sono obbligatori.";

          return;
        }


      /*  api.conf.filter({field:"k",value : $scope.conf.k}).$promise.then(function(r){
          if( r.length> 0 ){
            $scope.messaggioErrore = "La chiave scelta è già presente";
          }else{
            $scope.messaggioErrore = "ok";
          }
        })


        return;
*/
        if( $scope.conf.id !== undefined ){
          api.conf.put({id:$scope.conf.id},$scope.conf,
            function(r){
              $scope.answer(r);
            })

        }else{


          api.conf.post($scope.conf,
            function(r){
              $scope.answer(r);
            })
        }


      }
    }


    /** @ngInject */
    function ConfListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



      function loadConf(){
        api.conf.getAll({},
          function(result){
            vm.confList = result;
            $scope.tabella = new NgTableParams({}, { dataset : result});

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

        loadConf();

        $scope.aggiornaElemento = function(conf){

          api.conf.put({id : conf.id},conf);
        }
        $scope.modifica = function(ev,conf){
              $mdDialog.show({
                controller: ConfDialogController,
                templateUrl: 'app/main/impostazioni/conf/dialog-form.html',
                parent: angular.element(document.body),
                locals : {
                    item : conf
                },
                targetEvent: ev,
                clickOutsideToClose:false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
              })
              .then(function(answer) {
                 loadConf();
              }, function() {
                $scope.status = 'You cancelled the dialog.';
              });
        }

        $scope.showAdvanced = function(ev) {
               $mdDialog.show({
                 controller: ConfDialogController,
                 templateUrl: 'app/main/impostazioni/conf/dialog-form.html',
                 parent: angular.element(document.body),
                 targetEvent: ev,
                 locals : {
                     item : null
                 },
                 clickOutsideToClose:true,
                 fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
               })
             .then(function(answer) {
                loadConf();
             }, function() {
               $scope.status = 'You cancelled the dialog.';
             });
       };


    }



})();
