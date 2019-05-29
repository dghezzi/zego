(function ()
{
    'use strict';

    angular
        .module('app.promo.list',[
          'ngTable'
        ])
        .controller('PromoListController', PromoListController)
        .controller('PromoNewController', PromoNewController)
        .controller('PromoEditController', PromoEditController)
        .controller('PromoDialogController', PromoDialogController);



        function PromoDialogController($scope,$mdDialog,api,item){
          $scope.titoloDialog = "Aggiungi versione app";

          $scope.pattern = '[0-9]';


          if( item ){
            $scope.dialog = angular.copy(item);
            $scope.titoloDialog = "Modifica dialog";
            $scope.dialog.validfrom = new Date($scope.dialog.validfrom * 1000);
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
            var s = $scope.dialog;
            s.validfrom =   Math.round( s.validfrom.getTime() / 1000);
            if(s.id !== undefined ){

              api.dialog.put({id:s.id},s,
                function(){
                  alert("salvato");
                })

            }else{
              api.dialog.post(s,
                function(){
                  alert("salvato");
                })
            }


          }
        }







      function PromoNewController($scope,api,uiGmapIsReady,$state,$stateParams, $mdDialog){
        $scope.drawingManagerControl = {};
        $scope.serviziDisponibili = [];
        $scope.serviziAssociati = [];
        $scope.navigazione = {
          mode : 'new'
        }



        $scope.item = {
          type:'euro',
          feeonly:0,
          currentusages : 0,
          firstonly : 0
        };



        $scope.caricaPromoDaDuplicare = function(){
          if($stateParams.id){
         api.promo.get({id:$stateParams.id}).$promise.then(function(r){
           r.enablestart = new Date(r.enablestart * 1000);
           r.enablestop = new Date(r.enablestop * 1000);
           r.validfrom = new Date(r.validfrom * 1000);
           r.validto = new Date(r.validto * 1000);

           if(r.value){

             if(r.type=='euro' || r.type=='wallet'){
                r.value = r.value / 100;
             }



           }
           $scope.item = r;
           $scope.item.id=undefined;
            $scope.item.code=$scope.item.code+'_COPY';
           console.log(r);
         });


       }
      }
       $scope.caricaPromoDaDuplicare();


         $scope.salva = function(){
          if($scope.item && $scope.item.enablestart &&
           $scope.item.enablestop &&
           $scope.item.validfrom &&
           $scope.item.validto){
           var item = angular.copy($scope.item);


           if(item.type=='euro' || item.type=='wallet'){
             item.value = item.value.toString().replace(",",".") * 100;
           }

            item.enablestart.setHours(0,0,0,0);
         item.validfrom.setHours(0,0,0,0);
         item.validto.setHours(23,59,59,0);
         item.enablestop.setHours(23,59,59,0);

           item.insdate = Math.round( (new Date()) / 1000);
           item.moddate = Math.round( (new Date()) / 1000);
           for (var property in item) {
             if (item.hasOwnProperty(property) && item[property]) {
                 if(typeof item[property].getMonth === 'function'){
                     item[property] = Math.round(item[property].getTime() / 1000);
                 }
             }
           }


            api.promo.post(item).$promise.then(function(r){

               var confirm = $mdDialog.confirm()
              .title('Operazione riuscita')
              .textContent('Salvataggio effettuato correttamente')
              .targetEvent()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .ok('Torna alla lista');




        $mdDialog.show(confirm).finally(function() {
                          $state.go("app.promo");
        });

            }).catch( function(e){
              console.log(e);
              var text=e.statusText;
              var title='Si è verificato un errore';
              if(e.data){
                text=e.data.msg;
                title=e.data.title;
              }

                $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title(title)
                .textContent(text)
                .ok('Ok')
            );
            });
         }
       }

      }











    function PromoEditController($scope,api,uiGmapIsReady,$stateParams,$state,NgTableParams,$mdDialog){
      $scope.drawingManagerControl = {};
      $scope.serviziDisponibili = [];
      $scope.serviziAssociati = [];

      $scope.navigazione = {
        mode : 'edit'
      }

      $scope.userPromoDaInviare = [];


      $scope.inviaListaId = function(){
        var dialog = $mdDialog.show({
           controller: function(){},
           templateUrl: 'app/main/promo/popup-caricamento.html',
           parent: angular.element(document.body),
           clickOutsideToClose:false,
           fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
         })


        $scope.risultatiImportazioni =[];

      /*  for(var i = 0; i < $scope.userPromoDaInviare.length; i++) {

          var userpromo = {
            pid : $scope.item.id,
            uid : $scope.userPromoDaInviare[i],
            redeemdate : Math.round((new Date()).getTime() / 1000 ),
            expirydate : Math.round(($scope.item.validto).getTime() / 1000 ),
            valueleft : 0
          };

          (function(userpromo){
            api.promo.associaUser(userpromo).$promise.then(function(o){
                $scope.risultatiImportazioni.push("Associato a utente con id "+userpromo.uid);
                $scope.controllaImport();
            }).catch(function(){
              $scope.risultatiImportazioni.push("ERRORE : utente con id "+userpromo.uid);
              $scope.controllaImport();
            });
          })(userpromo);

        }*/


        var batchredeemObj={users: $scope.userPromoDaInviare, code: $scope.item.code};
       api.promo.batchredeem(batchredeemObj).$promise.then(function(o){
        console.log(o);
                 $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title('Operazione completata')
                .htmlContent(o.msg)
                .ok('Ok')
            );

            }).catch(function(e){
               var text=e.statusText;
              var title='Si è verificato un errore';
              if(e.data){
                text=e.data.msg;
                title=e.data.title;
              }

                $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title(title)
                .textContent(text)
                .ok('Ok')
            );
            });


}



/*
      $scope.controllaImport = function(){

          if($scope.risultatiImportazioni.length == $scope.userPromoDaInviare.length){
            $mdDialog.hide();
            getListaUtenti();
          }
      }
*/
      $scope.importaId = function(t){
        var f = t.files[0];
        //console.log(f);
        $scope.userPromoDaInviare = [];

        $scope.listaIdDaInviare = '';

        var fr = new FileReader();
        fr.onload = function(r){
          var r = fr.result;
          var a = r.split('\n');


          for (var i = 0; i < a.length; i++) {
             if( !a[i] || a[i]=='' || a[i]==null){
               continue;
             }
              if( $scope.idUtentiAssociati.indexOf(parseInt(a[i])) === -1 ){
                  $scope.userPromoDaInviare.push(a[i]);
                  if($scope.listaIdDaInviare!=''){
                    $scope.listaIdDaInviare=$scope.listaIdDaInviare+',';
                  }
                  $scope.listaIdDaInviare=$scope.listaIdDaInviare+a[i];
              }

          }


          $scope.$apply();

          if($scope.userPromoDaInviare.length == 0){
            $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title('Nessun id')
                .textContent('Non ci sono id validi a cui assegnare la promo.')
                .ok('ok')
            );

          }

        };
        //fr.readAsText(file);
        fr.readAsBinaryString(f);
      }


      function getListaUtenti(){
          api.promo.utentiAssociati({id:$scope.item.id}).$promise.then(function(p){
            $scope.utentiAssociati = p;
            $scope.idUtentiAssociati = [];

            for(var i =0; i< $scope.utentiAssociati.length; i++){
              $scope.idUtentiAssociati.push($scope.utentiAssociati[i].uid);
            }

            $scope.tabellaUtenti = new NgTableParams({},{dataset:p});
          })
      }

       $scope.caricaPromo = function(){
         api.promo.get({id:$stateParams.id}).$promise.then(function(r){
           r.enablestart = new Date(r.enablestart * 1000);
           r.enablestop = new Date(r.enablestop * 1000);
           r.validfrom = new Date(r.validfrom * 1000);
           r.validto = new Date(r.validto * 1000);

           if(r.value){

             if(r.type=='euro' || r.type=='wallet'){
                r.value = r.value / 100;
             }



           }
           $scope.item = r;

           console.log(r);
           getListaUtenti();
         });


       }
       $scope.caricaPromo();

       $scope.salva = function(){
         if($scope.item && $scope.item.enablestart &&
           $scope.item.enablestop &&
           $scope.item.validfrom &&
           $scope.item.validto){
         var item = angular.copy($scope.item);

         if(item.type=='euro' || item.type=='wallet'){
           item.value = item.value.toString().replace(",",".") * 100;
         }

          item.enablestart.setHours(0,0,0,0);
         item.validfrom.setHours(0,0,0,0);
         item.validto.setHours(23,59,59,0);
         item.enablestop.setHours(23,59,59,0);



         item.moddate = Math.round( (new Date()) / 1000);
         for (var property in item) {
           if (item.hasOwnProperty(property)) {
               if(typeof item[property].getMonth === 'function'){
                   item[property] = Math.round(item[property].getTime() / 1000);
               }
           }
         }

          api.promo.put({id: item.id},item).$promise.then(function(r){
            var confirm = $mdDialog.confirm()
              .title('Operazione riuscita')
              .textContent('Salvataggio effettuato correttamente')
              .targetEvent()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .ok('Torna alla lista');




        $mdDialog.show(confirm).finally(function() {
                          $state.go("app.promo");
        });
          }).catch( function(e){
              console.log(e);
              var text=e.statusText;
              var title='Si è verificato un errore';
              if(e.data){
                text=e.data.msg;
                title=e.data.title;
              }

                $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title(title)
                .textContent(text)
                .ok('Ok')
            );
            });
       }
     }



       $scope.prova = function(){

        /*
        NE 45.463983441272724 -71.74072265625
        SW 44.49650533109348 -74.0203857421875
        */


        console.log($scope.item.nwlat,$scope.item.selng);
        console.log($scope.item.selat,$scope.item.nwlng);

         var newBounds = new google.maps.LatLngBounds(
            new google.maps.LatLng($scope.item.selat,$scope.item.nwlng),
           new google.maps.LatLng($scope.item.nwlat,$scope.item.selng)

         );

        $scope.poligono.r = new google.maps.Rectangle({
          strokeColor: '#FF0000',
           strokeOpacity: 0.8,
           strokeWeight: 2,
           fillColor: '#FF0000',
           fillOpacity: 0.35,
            bounds: newBounds
          });

        console.log($scope.map.control.getGMap());

        $scope.poligono.r.set("bound",newBounds);
        $scope.poligono.r.setMap($scope.map.control.getGMap());

        $scope.map.control.getGMap().set("bounds",newBounds);


       }
       console.log($scope.drawingManagerControl);
    }






    /** @ngInject */
    function PromoListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



      function loadList(){
        api.promo.getAll({},
          function(result){
            vm.list = result;
            $scope.tabella = new NgTableParams({}, { dataset : vm.list});

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

        loadList();

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

        $scope.exportAll = function(){

            api.advancedExport({
               entity :'promo',
               start  :0,
               stop : 50000
             },{},'export-promo');
     }



    }



})();
