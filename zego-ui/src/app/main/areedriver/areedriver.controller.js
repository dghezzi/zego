(function ()
{
    'use strict';

    angular
        .module('app.aree.list',[
          'ngTable'
        ])
        .controller('AreeListController', AreeListController)
        .controller('AreeDialogController', AreeDialogController);



    function AreeDialogController($scope,$mdDialog,api,item){
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

          api.area.put({id:$scope.item.id},$scope.item,
            function(r){
              $scope.answer(r);
            })

        }else{
          $scope.item.insdate = Math.round( new Date().getTime() / 1000 )
          api.area.post($scope.item,
            function(r){
              $scope.answer(r);
            })
        }


      }
    }


    /** @ngInject */
    function AreeListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



          $scope.delete = function(item){
            api.area.delete({id:item.id}).$promise.then(function(r){
              loadList();
            }).catch(function(){
              loadList();
            })
          }
      function loadList(){
        api.area.getAll({},
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
                controller: AreeDialogController,
                templateUrl: 'app/main/areedriver/dialog-form.html',
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
                 controller: AreeDialogController,
                 templateUrl: 'app/main/areedriver/dialog-form.html',
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
