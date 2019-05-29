(function ()
{
    'use strict';

    angular
        .module('app.errormsg.list',[])
        .controller('ErrorMsgListController', ErrorMsgListController)
        .controller('ErrorMsgDialogController', ErrorMsgDialogController);


      function ErrorMsgDialogController($timeout,$scope,api,$mdDialog,item){

        $scope.titoloDialog = "Aggiungi messaggio di errore";
        if( item ){
          $scope.errormsg = item;
          $scope.titoloDialog = "Modifica messaggio di errore";
        }

        $scope.lingueDisponibili = [];
        api.lang.getAll({},
        function(r){
          $scope.lingueDisponibili = r;
          console.log(r);
        });

        $scope.cancel = function() {
          $mdDialog.cancel();
        };
        $scope.answer = function(answer) {
          $mdDialog.hide(answer);
        };

        $scope.salva = function(){

          if(!$scope.errormsg || !$scope.errormsg.code || !$scope.errormsg.lang || !$scope.errormsg.title || !$scope.errormsg.message ){
            $scope.messaggioErrore = "Tutti i campi sono obbligatori.";

            return;
          }
          $scope.loadingSalva = true;
          if($scope.errormsg.id!==undefined){
            api.errormsg.put({id:$scope.errormsg.id},$scope.errormsg,
              function(){
                $scope.answer(true);
                $scope.loadingSalva = false;
              })
          }else{

            api.errormsg.filter({field: "code",value :$scope.errormsg.code}).$promise.then(function(r){
              var presente = false;
              $scope.loadingSalva = true;

              if( r.length > 0 ){
                angular.forEach(r,function(o){
                  if( o.lang == $scope.errormsg.lang ){
                    presente = true;
                  }
                })
              }


              if(presente){
                $scope.messaggioErrore = "Il codice è già presente nella lingua scelta.";

              }else{
                api.errormsg.post($scope.errormsg,function(r){
                  $scope.answer(r);
                });
              }


              console.log(r);
            })

          }
        }
      }
    /** @ngInject */
    function ErrorMsgListController(api,$scope,$mdDialog,NgTableParams)
    {



      var vm = this;



      function loadList(){
        api.errormsg.getAll({},
          function(result){
            vm.list = result;
            $scope.tabella = new NgTableParams({},{dataset : vm.list})
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


 $scope.exportAll = function(){



            api.advancedExport({
               entity :'errormsg',
               start  :0,
               stop : 50000
             },{},'export-errormsg');
     }




        $scope.modifica = function(ev,conf){
              $mdDialog.show({
                controller: ErrorMsgDialogController,
                templateUrl: 'app/main/impostazioni/errormsg/dialog-form.html',
                parent: angular.element(document.body),
                locals : {
                    item : conf
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
           controller: ErrorMsgDialogController,
           templateUrl: 'app/main/impostazioni/errormsg/dialog-form.html',
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
