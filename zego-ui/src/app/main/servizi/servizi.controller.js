(function ()
{
    'use strict';

    angular
        .module('app.servizi.list',[
          'ngTable'
        ])
        .controller('ServiziListController', ServiziListController)
        .controller('ServiziDialogController', ServiziDialogController);



    function ServiziDialogController($scope,$mdDialog,api,item){
      $scope.titoloDialog = "Aggiungi servizio";


      if( item ){
        $scope.item = angular.copy( item );
        $scope.titoloDialog = "Modifica servizio";
      }




       $scope.hours = [];

      for (var i=0; i<24; i++){
        $scope.hours.push({label: ('0'+i).slice(-2), value: i});
      }

      $scope.minutes = [{label: '00', value: 0},{label: '30', value: 30}];



      var from=$scope.item.fromhr;
      $scope.item.fromhr=Math.floor(from/60);
       $scope.item.frommin=from-($scope.item.fromhr*60);

       var to=$scope.item.tohr;
      $scope.item.tohr=Math.floor(to/60);
       $scope.item.tomin=to-($scope.item.tohr*60);



      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){

        console.log($scope.item);
          if($scope.item.fromhr!=undefined && $scope.item.frommin!=undefined){
                  $scope.item.fromhr=$scope.item.fromhr*60+$scope.item.frommin;
                  console.log($scope.item.fromhr);
            }

            if($scope.item.tohr!=undefined && $scope.item.tomin!=undefined){
                  $scope.item.tohr=$scope.item.tohr*60+$scope.item.tomin;
            }

            console.log($scope.item);
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
    function ServiziListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;




          $scope.delete = function(item){
            api.servizi.delete({id:item.id}).$promise.then(function(r){
              loadList();
            }).catch(function(){
              loadList();
            })
          }
      function loadList(){
        api.servizi.getAll({},
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
