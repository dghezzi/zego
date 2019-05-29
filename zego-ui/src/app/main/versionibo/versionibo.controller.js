(function ()
{
    'use strict';

  /*  angular
        .module('app.zone.list',[
          'ngTable'
        ])/*
        .controller('ZoneListController', ZoneListController)
        .controller('ZoneNewController', ZoneNewController)
        .controller('ZoneEditController', ZoneEditController)
        .controller('ZoneDialogController', ZoneDialogController)*/;



        function ZoneDialogController($scope,$mdDialog,api,item){
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







      function ZoneNewController($scope,api,uiGmapIsReady,$state){
        $scope.drawingManagerControl = {};
        $scope.serviziDisponibili = [];
        $scope.serviziAssociati = [];
        $scope.navigazione = {
          mode : 'new'
        }

        $scope.item = {};

        $scope.map = { center: { latitude: 45, longitude: 12 }, zoom: 7,
        control : {}
      };


        uiGmapIsReady.promise(1).then(function(instances) {


             instances.forEach(function(inst) {

                 api.zone.getAll().$promise.then(function(r){
                    angular.forEach(r,function(o){

                        var newBounds = new google.maps.LatLngBounds(
                           new google.maps.LatLng(o.selat,o.nwlng),
                          new google.maps.LatLng(o.nwlat,o.selng)

                        );

                        var p = new google.maps.Rectangle({
                          strokeColor: '#FF0000',
                           strokeOpacity: 0.2,
                           strokeWeight: 2,
                           fillColor: '#FF0000',
                           fillOpacity: 0.1,
                            bounds: newBounds
                          });



                        p.set("bound",newBounds);
                        p.setMap($scope.map.control.getGMap());


                    });
                 });
                 google.maps.event.addListener(  $scope.drawingManagerControl.getDrawingManager(), 'overlaycomplete', function(e) {
                    $scope.$apply(function(){
                      $scope.poligono.r = e.overlay;

                    });
                });
             });
         });


        $scope.poligono = {r : null};
        $scope.ricomincia = function(){
          if($scope.poligono.r){
            $scope.poligono.r.setMap(null);
          }

          $scope.poligono = {r:null};
        }
        $scope.$watch("poligono.r",function(n){

          if(n){
            $scope.drawingManagerOptions.drawingControl = false;
            $scope.drawingManagerOptions.drawingMode = null;
          }else{
            $scope.drawingManagerOptions.drawingControl = true;
            $scope.drawingManagerOptions.drawingMode = google.maps.drawing.OverlayType.RECTANGLE;
          }
        })

        $scope.drawingManagerOptions = {
         drawingMode: google.maps.drawing.OverlayType.RECTANGLE,
         drawingControl: true,
         drawingControlOptions: {
           position: google.maps.ControlPosition.TOP_CENTER,
             drawingModes: [
               google.maps.drawing.OverlayType.RECTANGLE,

             ]
         }
         };


         $scope.caricaServiziDisponibili = function(){
           api.servizi.getAll().$promise.then(function(r){
             $scope.serviziDisponibili = r;
           })
         }


         $scope.caricaServiziDisponibili();



         $scope.salva = function(){
            console.log($scope.item);
            var bounds = $scope.poligono.r.getBounds();
            var NE = bounds.getNorthEast();
            var SW = bounds.getSouthWest();
            // North West

            $scope.item.nwlat = NE.lat();
            $scope.item.nwlng = SW.lng();
            $scope.item.selat = SW.lat();
            $scope.item.selng = NE.lng();


            $scope.item.insdate = Math.round((new Date()).getTime() / 1000 );

            api.zone.post($scope.item).$promise.then(function(r){

              angular.forEach($scope.serviziAssociati,function(o,k){
                if(o.id == undefined){
                  // non è salvato
                  if(o.check == true){
                    var s = {
                      zid : r.id,
                      sid : k,
                      insdate : Math.round((new Date()).getTime() /1000)
                    }
                    api.zone.addService(s);
                  }
                }else{

                  if(o.check == false){
                      api.zone.removeService({id:o.id});
                      o.id=undefined;
                  }
                }
              })

              $state.go("app.zone");

            })
         }
         console.log($scope.drawingManagerControl);
      }











    function ZoneEditController($scope,api,uiGmapIsReady,$stateParams,$state){
      $scope.drawingManagerControl = {};
      $scope.serviziDisponibili = [];
      $scope.serviziAssociati = [];

      $scope.navigazione = {
        mode : 'edit'
      }

      $scope.item = {};

      $scope.map = { center: { latitude: 45, longitude: 12 }, zoom: 7,
      control : {}
    };


      uiGmapIsReady.promise(1).then(function(instances) {

           instances.forEach(function(inst) {

               google.maps.event.addListener(  $scope.drawingManagerControl.getDrawingManager(), 'overlaycomplete', function(e) {
                  $scope.$apply(function(){
                    $scope.poligono.r = e.overlay;

                  });
              });
           });
       });


      $scope.poligono = {r : null};
      $scope.ricomincia = function(){
        if($scope.poligono.r){
          $scope.poligono.r.setMap(null);
        }

        $scope.poligono = {r:null};
      }
      $scope.$watch("poligono.r",function(n){

        if(n){
          $scope.drawingManagerOptions.drawingControl = false;
          $scope.drawingManagerOptions.drawingMode = null;
        }else{
          $scope.drawingManagerOptions.drawingControl = true;
          $scope.drawingManagerOptions.drawingMode = google.maps.drawing.OverlayType.RECTANGLE;
        }
      })

      $scope.drawingManagerOptions = {
       drawingMode: google.maps.drawing.OverlayType.RECTANGLE,
       drawingControl: true,
       drawingControlOptions: {
         position: google.maps.ControlPosition.TOP_CENTER,
           drawingModes: [

             google.maps.drawing.OverlayType.RECTANGLE,

           ]
       }
       };


       $scope.caricaServiziDisponibili = function(){
         api.servizi.getAll().$promise.then(function(r){
           $scope.serviziDisponibili = r;
         })
       }


       $scope.caricaServiziDisponibili();

       $scope.caricaZona = function(){



         api.zone.get({id:$stateParams.id}).$promise.then(function(r){

           $scope.item = r;

           api.zone.getAll().$promise.then(function(r){
              angular.forEach(r,function(o){
                if($scope.item.id != o .id){
                  var newBounds = new google.maps.LatLngBounds(
                     new google.maps.LatLng(o.selat,o.nwlng),
                    new google.maps.LatLng(o.nwlat,o.selng)

                  );

                  var p = new google.maps.Rectangle({
                    strokeColor: '#FF0000',
                     strokeOpacity: 0.2,
                     strokeWeight: 2,
                     fillColor: '#FF0000',
                     fillOpacity: 0.1,
                      bounds: newBounds
                    });



                  p.set("bound",newBounds);
                  p.setMap($scope.map.control.getGMap());

                }
              });
           });

           api.zone.getService({id:$scope.item.id}).$promise.then(function(r){
              $scope.serviziAssociati = [];

              angular.forEach(r,function(o){


                  o.check = true;
                  $scope.serviziAssociati[o.sid] = o;
              });
           });

           var newBounds = new google.maps.LatLngBounds(
              new google.maps.LatLng($scope.item.selat,$scope.item.nwlng),
             new google.maps.LatLng($scope.item.nwlat,$scope.item.selng)

           );

          $scope.poligono.r = new google.maps.Rectangle({
            strokeColor: '#0000FF',
             strokeOpacity: 0.8,
             strokeWeight: 2,
             fillColor: '#0000FF',
             fillOpacity: 0.35,
              bounds: newBounds
            });



          $scope.poligono.r.set("bound",newBounds);
          $scope.poligono.r.setMap($scope.map.control.getGMap());

          $scope.map.control.getGMap().set("bounds",newBounds);

         });
       }
       $scope.caricaZona();

       $scope.salva = function(){

          var bounds = $scope.poligono.r.getBounds();
          var NE = bounds.getNorthEast();
          var SW = bounds.getSouthWest();
          // North West

          $scope.item.nwlat = NE.lat();
          $scope.item.nwlng = SW.lng();
          $scope.item.selat = SW.lat();
          $scope.item.selng = NE.lng();


          $scope.item.insdate = Math.round((new Date()).getTime() / 1000 );

          angular.forEach($scope.serviziAssociati,function(o,k){
            if(o.id == undefined){
              // non è salvato
              if(o.check == true){
                var s = {
                  zid : $scope.item.id,
                  sid : k,
                  insdate : Math.round((new Date()).getTime() /1000)
                }
                api.zone.addService(s);
              }
            }else{

              if(o.check == false){
                  api.zone.removeService({id:o.id});
                  o.id=undefined;
              }
            }
          })

          api.zone.put({id: $scope.item.id},$scope.item).$promise.then(function(r){
            $state.go("app.zone");
          })
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
    function ZoneListController(api,$scope,$mdDialog,NgTableParams)
    {

      var vm = this;



      function loadList(){
        api.zone.getAll({},
          function(result){
            vm.list = result;
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
                templateUrl: '/app/main/dialog/dialog-form.html',
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
                 templateUrl: '/app/main/dialog/dialog-form.html',
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
