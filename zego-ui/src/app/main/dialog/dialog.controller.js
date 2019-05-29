(function ()
{
    'use strict';

    angular
        .module('app.dialog.list',[
          'ngTable'
        ])
        .controller('DialogListController', DialogListController)
        .controller('DialogDialogController', DialogDialogController);



    function DialogDialogController($scope,$mdDialog,api,item){
      $scope.titoloDialog = "Aggiungi dialog";

      $scope.pattern = '[0-9]';


      if( item ){
        $scope.dialog = angular.copy(item);
        $scope.titoloDialog = "Modifica dialog";
        $scope.dialog.validfrom = new Date($scope.dialog.validfrom * 1000);
        $scope.dialog.validto = new Date($scope.dialog.validto * 1000);
      }

      if(item && item.type !== undefined && item.type=="tc"){
        $scope.titoloDialog = "Modifica termini e condizioni";
      }

      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){

        var s = angular.copy($scope.dialog);
          if (s.validto && s.validfrom){
            s.validfrom.setHours(0,0,0,0);
            s.validto.setHours(23,59,59,0);
        s.validfrom =   Math.round( s.validfrom.getTime() / 1000);
        s.validto =   Math.round( s.validto.getTime() / 1000);
        s.type='inapp';
        if(s.id !== undefined ){

          api.dialog.put({id:s.id},s,
            function(r){
              $scope.answer(r);
            })

        }else{
          api.dialog.post(s,
            function(r){
              $scope.answer(r);
            })
        }
}

      }
    }


    /** @ngInject */
    function DialogListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



      function loaddialog(){
        api.dialog.getAll({},
          function(result){
            vm.dialogList = result;
            //vm.tabella = new NgTableParams({}, { dataset : vm.dialogList});

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

        loaddialog();

        $scope.aggiornaElemento = function(dialog){

          api.dialog.put({id : dialog.id},dialog);
        }
        $scope.modifica = function(ev,dialog){
              $mdDialog.show({
                controller: DialogDialogController,
                templateUrl: 'app/main/dialog/dialog-form.html',
                parent: angular.element(document.body),
                locals : {
                    item : dialog
                },
                targetEvent: ev,
                clickOutsideToClose:false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
              })
              .then(function(answer) {
                 loaddialog();
              }, function() {
                $scope.status = 'You cancelled the dialog.';
              });
        }

        $scope.showAdvanced = function(ev) {
               $mdDialog.show({
                 controller: DialogDialogController,
                 templateUrl: 'app/main/dialog/dialog-form.html',
                 parent: angular.element(document.body),
                 targetEvent: ev,
                 locals : {
                     item : null
                 },
                 clickOutsideToClose:true,
                 fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
               })
             .then(function(answer) {
                loaddialog();
             }, function() {
               $scope.status = 'You cancelled the dialog.';
             });
       };





    }



})();
