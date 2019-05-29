(function ()
{
    'use strict';

    angular
        .module('app.blacklistdevice.list',[
          'ngTable'
        ])
        .controller('BlacklistdeviceListController', BlacklistdeviceListController)
        .controller('BlacklistDialogController', BlacklistDialogController);



    function BlacklistDialogController($scope,$mdDialog,api,item){
      $scope.titoloDialog = "Aggiungi servizio";


      if( item ){
        $scope.item = angular.copy( item );
        $scope.titoloDialog = "Modifica servizio";
      }



      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){

        if( $scope.item.id !== undefined ){

          api.servizi.put({id:$scope.item.id},$scope.item,
            function(r){
              $scope.answer(r);
            })

        }else{
          $scope.item.insdate = Math.round( new Date().getTime() / 1000 )
          api.servizi.post($scope.item,
            function(r){
              $scope.answer(r);
            })
        }


      }
    }


    /** @ngInject */
    function BlacklistdeviceListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



          $scope.delete = function(item){
            api.blacklist.elimina({id:item.id}).$promise.then(function(r){
              loadList();
            }).catch(function(){
              loadList();
            })
          }

      function loadList(){
        api.blacklist.getAll({},
          function(result){
            vm.list = result;
            $scope.tabella = new NgTableParams({}, { dataset : vm.list});

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

        loadList();

        $scope.modifica = function(ev,s){
              $mdDialog.show({
                controller: ServiziDialogController,
                templateUrl: 'app/main/servizi/dialog-form.html',
                parent: angular.element(document.body),
                locals : {
                    item : s
                },
                targetEvent: ev,
                clickOutsideToClose:false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
              })
              .then(function(answer) {
                 loadList();
              }, function() {
                $scope.status = 'You cancelled the dialog.';
              });
        }

        $scope.showAdvanced = function(ev) {
               $mdDialog.show({
                 controller: ServiziDialogController,
                 templateUrl: 'app/main/servizi/dialog-form.html',
                 parent: angular.element(document.body),
                 targetEvent: ev,
                 locals : {
                     item : null
                 },
                 clickOutsideToClose:true,
                 fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
               })
             .then(function(answer) {
                loadList();
             }, function() {
               $scope.status = 'You cancelled the dialog.';
             });
       };


    }



})();
