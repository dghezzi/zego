(function ()
{
    'use strict';

    angular
        .module('app.lang.list',[])
        .controller('LangListController', LangListController)
        .controller('LangDialogController', LangListController);



    function LangDialogController(api,$scope,$mdDialog,item){
      var vm = this;

      $scope.titoloDialog = "Aggiungi lingua";
      if( item ){
        $scope.linguaSelezionata = item;
        $scope.lingua = item;
        $scope.titoloDialog = "Modifica lingua";
      }

      console.log($scope.linguaSelezionata);
      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };

      $scope.lingueDisponibili  = api.langIsoAll();

      $scope.salva = function(){
        console.log($scope.linguaSelezionata);

        if(!$scope.linguaSelezionata)
        return;

        if(!$scope.linguaSelezionata.lang){
          $scope.messaggioErrore = "Scegli la lingua per salvare";
          return;
        }
        if(!$scope.linguaSelezionata.isocode){
          $scope.messaggioErrore = "Inserisci l'isocode per salvare";
          return;
        }


        var l = {
          lang : $scope.linguaSelezionata.lang,
          isocode : $scope.linguaSelezionata.isocode,
          sort : 0
        }



        if( $scope.linguaSelezionata ){


          api.lang.filter({
            field : 'isocode',
            value : $scope.linguaSelezionata.isocode
          }).$promise.then(function(o){
              console.log(o);
              if(o.length > 0){
                  $scope.errore = "La lingua selezionata è già presente."
              }else{
                  $scope.errore = '';
                  if($scope.linguaSelezionata.id != undefined){
                    api.lang.put({id:$scope.linguaSelezionata.id},$scope.linguaSelezionata,
                      function(r){
                          $scope.answer(r);
                      },
                      function(error){
                          $scope.answer(error);
                      }

                    );
                  }else{

                        api.lang.post(l,
                          function(r){
                              $scope.answer(r);
                          },
                          function(error){
                              $scope.answer(error);
                          }

                        );

                  }
                  
              }
          });




        }


      }

    }
    /** @ngInject */
    function LangListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;




      function loadLang(){
        api.lang.getAll({},
          function(result){
            vm.lingue = result;
            $scope.tabella = new NgTableParams({},{dataset : vm.lingue})
            console.log(result);
          },
          function(error){
            console.log(error);
          }
        );
      }

      loadLang();

      $scope.delete = function(item){
        api.lang.delete({id:item.id}).$promise.then(function(r){
          loadLang();
        }).catch(function(){
          loadLang();
        })
      }
      var lingueDisponibili = api.langIsoAll();
        // Data

        // Methods

        //////////

        $scope.modifica = function(ev,lang){
              $mdDialog.show({
                controller: LangDialogController,
                templateUrl: 'app/main/impostazioni/lang/dialog-form.html',
                parent: angular.element(document.body),
                locals : {
                    item : lang
                },
                targetEvent: ev,
                clickOutsideToClose:false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
              })
              .then(function(answer) {
                  loadLang();
              }, function() {
                $scope.status = 'You cancelled the dialog.';
              });
        }




      $scope.showAdvanced = function(ev) {
       $mdDialog.show({
         controller: LangDialogController,
         templateUrl: 'app/main/impostazioni/lang/dialog-form.html',
         parent: angular.element(document.body),
         targetEvent: ev,
         locals : {
             item : null
         },
         clickOutsideToClose:true,
         fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
       })
       .then(function(answer) {
            loadLang();
       }, function() {
         $scope.status = 'You cancelled the dialog.';
       });
     };


    }



})();
