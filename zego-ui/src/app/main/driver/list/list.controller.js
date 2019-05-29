(function ()
{
    'use strict';

    angular
        .module('app.driver.list')
        .controller('DriverListController', DriverListController)

        .controller("EditDriverController",EditDriverController);
    /** @ngInject */
    function DriverListController(api,$scope,$window,NgTableParams,$mdDialog)
    {


 $scope.csv={};

    $scope.visualizzaCsv = function(){
      console.log('visualizzaCsv');
    }

    $scope.inviaCsv = function(){
      console.log('inviaCsv');

            api.driverdata.sendCsv({},$scope.csv.result,
            function(r){
              alert('csv inviato');
               $scope.anteprimaCsv=false;
               $scope.csv={};
            },function(e){
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


      var vm = this;
      vm.window = $window;

      $scope.rtstatus = [];

      $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_IDLE', value : 100});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_REQUEST_SENT', value : 101});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_WAITING_DRIVER', value : 102});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_ONRIDE', value : 103});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_PAYMENT_DUE', value : 104});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_FEEDBACK_DUE', value : 105});

     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_IDLE', value : 200});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_ANSWERING', value : 201});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_PICKINGUP', value : 202});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_ONRIDE', value : 203});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_FEEDBACKDUE', value : 204});


     $scope.status ={};
     $scope.status[1] = "Richiesta effettuata";
     $scope.status[2] = "Documenti accettati";
     $scope.status[3] = "Documenti respinti";
     $scope.status[4] = "In attesa di nuovi documenti";
     $scope.status[5] = "Documenti inviati, da controllare";
     $scope.status[6] = "Documenti scaduti";

      console.log(window.width);
      vm.filter = {};

      vm.paginationValues = [5,10,25,50];

      var tableColumnsBase = {
            "country": false,
            "device": false,
            "deviceid": false,
            "id": false,
            "insdate": true,
            "lastseen": true,
            "moddate": false,
            "status": false,
            "umode": false,
            "utype": false,
            "vapp": false,
            "vos": false,
            "zegotoken": false,
            "refcode": false ,
            "email": true ,
            "fullname": true,
            "phone":true
      }



        $scope.exportAll = function(){



                  api.advancedExport({
                     entity :'driverdata',
                     start  :0,
                     stop : 50000
                   },$scope.ultimaRicerca,'export-driver');
          }



      $scope.tableColumns = angular.copy(tableColumnsBase);

      angular.element($window).bind('resize', function(){
          /*$scope.tableColumns = angular.copy(tableColumnsBase);
         var width = $window.innerWidth;
         if(width < 1440){
            $scope.tableColumns.insdate = false;
         }

         if(width < 1300){
            $scope.tableColumns.insdate = false;
         }*/

      //   $scope.$digest();
       });


      $scope.pagination = {
        page : 0,
        start : 0,
        stop : 10,
        num : 10
      };


      $scope.$watch("pagination.num",function(){
          $scope.aggiornaPaginazione ();
          loadList();
      })



      $scope.resettaFiltri = function(){
        vm.filter = {};
      }

      $scope.resettaFiltri();


      $scope.numeroUtenti = 0;
      function getCountAll(){
          api.user.list.count().$promise.then(function(r){
              $scope.numeroUtenti = r.ct;
          });

      }
      //getCountAll();


      $scope.numeroDrivers = 0;
      function getCountDrivers(){
          var ricerca = new api.oggettoRicerca();


          ricerca.buildFilters({
            simple:{
              umode : 'driver'
            }
          });

          api.advancedSearch.post({
             entity :'user',
             start  :0,
             stop : 1000
           },ricerca).$promise.then(function(data){
             $scope.numeroDrivers = data.length;
           });
      }
      //getCountDrivers();

      $scope.numeroRiders = 0;
      function getCountRiders(){
          var ricerca = new api.oggettoRicerca();


          ricerca.buildFilters({
            simple:{
              umode : 'rider'
            }
          });

          api.advancedSearch.post({
             entity :'user',
             start  :0,
             stop : 1000
           },ricerca).$promise.then(function(data){
             $scope.numeroRiders = data.length;
           });
      }
      //getCountRiders();



      $scope.numeroLastWeek = 0;
      function getCountLastWeek(){
          var ricerca = new api.oggettoRicerca();


          ricerca.buildFilters({
            range:{
              insdate : {
                from : Math.round( ( (new Date().getTime()) - (3600 * 1000 * 24 * 7 ) ) / 1000 ),
                to : Math.round( new Date().getTime() / 1000 )
              }
            }
          });

          api.advancedSearch.post({
             entity :'user',
             start  :0,
             stop : 1000
           },ricerca).$promise.then(function(data){
             $scope.numeroLastWeek = data.length;
           });
      }
      //getCountLastWeek();

      $scope.vediSoloDrivers = function(){
        $scope.titoloRicerca = "Drivers";
        vm.filter = {
          simple : {
            umode : 'driver'
          }
        }

        $scope.tabella.reload();
        $scope.filtriVisibili = false;

      }


      $scope.vediSoloRiders = function(){
        $scope.titoloRicerca = "Riders";
        vm.filter = {
          simple : {
            umode : 'rider'
          }
        }

        $scope.tabella.reload();
        $scope.filtriVisibili = false;

      }
      $scope.vediUltimiIscritti = function(){
        $scope.titoloRicerca = "Utenti iscritti nell'ultima settimana";
        vm.filter = {
          direction : 'desc',
          sortfield : 'insdate',
          range:{
            insdate : {
              from : Math.round( ( (new Date().getTime()) - (3600 * 1000 * 24 * 7 ) ) / 1000 ),
              to : Math.round( new Date().getTime() / 1000 )
            }
          }
        }

        $scope.tabella.reload();
        $scope.filtriVisibili = false;

      }
      $scope.vediTutti = function(){
        $scope.titoloRicerca = "Tutti gli utenti";
        vm.filter = {
        }

        $scope.tabella.reload();
        $scope.filtriVisibili = false;

      }

      $scope.delete = function(user){
        var confirm = $mdDialog.confirm()
              .title('Elimina utente?')
              .textContent('Eliminando l\'utente non sarà più possibile recuperarlo, sei sicuro di voler continuare?')
              .targetEvent()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .ok('Elimina utente')
              .cancel('No, annulla');
        $mdDialog.show(confirm).then(function() {
            api.user.single.delete({id : user.id}).$promise.then(function(r){
              $scope.tabella.reload();
            });

        }, function() {

        });


      }


      $scope.aggiornaPaginazione = function(){
        $scope.pagination.start = $scope.pagination.page * $scope.pagination.num;
        $scope.pagination.stop = ( $scope.pagination.page * $scope.pagination.num ) + parseInt($scope.pagination.num) ;
      }
        // Data

        // Methods

        vm.nextPage = function(){
          $scope.pagination.page += 1;
          $scope.pagination.start = $scope.pagination.page * $scope.pagination.num;
          $scope.pagination.stop = ( $scope.pagination.page * $scope.pagination.num ) + parseInt($scope.pagination.num) ;
          loadList();
        }
        vm.prevPage = function(){
          $scope.pagination.page -= 1;
          if($scope.pagination.page <0)
            $scope.pagination.page =0;

          $scope.pagination.start = $scope.pagination.page * $scope.pagination.num;
          $scope.pagination.stop = ($scope.pagination.page * $scope.pagination.num) + parseInt($scope.pagination.num) ;
          loadList();
        }

        $scope.salvaFiltri  = function(){
          $scope.tabella.page(1);
          $scope.tabella.reload();
          $scope.filtriVisibili = false;
        }




        /*
        function loadList(){

          $scope.loadingTable = true;
          api.driverdata.withstatus({
                status : 1,
                start : 0,
                stop: 1000000,
                sort: 'datedesc'
           }).$promise.then(function(data){

             $scope.statusFilter = data;
             $scope.drivers = data;
             angular.forEach(data,function(o){
                 for (var property in o.user) {
                     if (o.user.hasOwnProperty(property)) {
                         o['user'+property] = o.user[property];
                     }
                  }

                  o.status = $scope.status[o.status];
             })

              $scope.tabella = new NgTableParams({},{dataset : data});
              $scope.loadingTable = false;
           });

        }
        loadList();

*/



        // inizio nuova
        function loadList(){

          $scope.tabella = new NgTableParams({}, {

            getData: function(params) {
             // ajax request to api
              $scope.loadingTable = true;
             var ord = params.orderBy();

             var ricerca = new api.oggettoRicerca();



             ricerca.buildFilters(vm.filter);



             /*api.advancedSearch.post({
               entity :'user',
               start  :'0',
               stop : '100'
             },ricerca).$promise.then(function(r){
               console.log(r);
             });*/




             if( ord.length > 0 ){
               vm.filter.order = ord[0].substr(0,1) == '+' ? "desc" : "asc";
               vm.filter.orderby = ord[0].substr(1,ord[0].length-1);
               ricerca.orderBy(vm.filter.orderby,vm.filter.order);
             }
             vm.filter.page = params.page()-1;
             console.log(ricerca);
             var start = (params.page() - 1) * params.count();
             var stop = params.page() * params.count();




            $scope.ultimaRicerca = ricerca;

            return  api.advancedSearch.post({
               entity :'driverdata',
               start  :start,
               stop : stop
             },ricerca).$promise.then(function(data){

                  console.log(data);

                $scope.drivers  = data;

                return api.advancedCount.post({
                   entity :'driverdata'
                 },ricerca).$promise.then(function(r){
                   params.total(r.ct);
                    $scope.loadingTable = false;


                  angular.forEach(data,function(o){
                       for (var property in o.user) {
                           if (o.user.hasOwnProperty(property)) {
                               o['user'+property] = o.user[property];
                           }
                        }
                   });



                   return data;
                 })


                /*return api.user.list.count().$promise.then(function(r){

                  params.total(r.ct);

                  return data;
                })*/



             });



             /*
                   return api.user.list.pag(
                     {
                       page : params.page() - 1,
                       start : start,
                       stop : stop,
                       num : params.count()
                     }
                   ).$promise.then(function(data) {
                     //params.total(500); // recal. page nav controls

                        vm.users = data;
                         return api.user.list.count().$promise.then(function(r){

                           params.total(r.ct);

                           return data;
                         })

                   });
                   */



           },
           paginationMaxBlocks: 13,
           paginationMinBlocks: 2
          });


          /*
          api.user.list.pag($scope.pagination,

            function(result){
              vm.users = result;

              //$scope.tabella = new NgTableParams({},{dataset : vm.users})






            },
            function(error){
              console.log(error);
            }
          );
          */
        }
        loadList();

        //////////
    }



    function EditDriverController(api,$stateParams,$scope,$mdDialog,$state,NgTableParams)
    {
      $scope.rtstatus = [];

      $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_IDLE', value : 100});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_REQUEST_SENT', value : 101});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_WAITING_DRIVER', value : 102});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_ONRIDE', value : 103});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_PAYMENT_DUE', value : 104});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_PASSENGER_FEEDBACK_DUE', value : 105});

     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_IDLE', value : 200});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_ANSWERING', value : 201});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_PICKINGUP', value : 202});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_ONRIDE', value : 203});
     $scope.rtstatus.push({'label' : 'REALTIME_STATUS_DRIVER_FEEDBACKDUE', value : 204});

      $scope.requestStatusLabel = api.requestStatusLabel;

      $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };


      $scope.searchPromo = function(t){


        return api.promo.search({pfx:t}).$promise.then(function(o){
          var dati = [];
          var associateIDS = [];
          if($scope.promoAssociate){
          angular.forEach($scope.promoAssociate,function(p){
            associateIDS.push(p.pid);
          });


          angular.forEach(o,function(p){

            if (true || associateIDS.indexOf(p.id) == -1 ){
              dati.push(p);
            }
          })

          }

          return dati;
          /*angular.forEach(o,function(p){
            var trovata = false;
            console.log($scope.promoAssociate);
            angular.forEach($scope.promoAssociate,function(pa){
              if(pa.pid == p.id)
              trovata = true;
            })

            if(!trovata){
              dati.push(p);
            }

          })*/

        });
      }

      $scope.$watch('nuovapromo',function(){
        console.log($scope.nuovapromo);
        if($scope.nuovapromo){
          var userpromo = {
            pid : $scope.nuovapromo.id,
            uid : vm.user.id,
            redeemdate : Math.round((new Date()).getTime() / 1000 ),
            expirydate : $scope.nuovapromo.validto,
            valueleft : 0
          };

          api.promo.associaUser(userpromo).$promise.then(function(o){
              getListaPromozioni();
          });

          $scope.nuovapromo = null;


        }
      })

      $scope.deletePromo = function(promo){
        api.promo.disassociaUtente({id:promo.id}).$promise.then(function(o){
          getListaPromozioni();
        })
      }

      function getListaPromozioni(){
        api.promo.byUser({uid : vm.user.id}).$promise.then(function(r){
          $scope.promoAssociate = r;
          $scope.tabellaPromoAssociate = new NgTableParams({},{dataset : $scope.promoAssociate});
        })
      }
      $scope.inviaPin = function(){
        api.user.single.resend({uid:vm.user.id}).$promise.then(function(o){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Pin inviato')
              .textContent('Pin inviato correttamente.')
              .ok('Go')
          );

          getListaPin();
        })
      }
      $scope.vediFoto = function(ev) {
        $scope.fotoDialog = $mdDialog.show({

          contentElement: '#myDialog',
          parent: angular.element(document.body),
          targetEvent: ev,
          clickOutsideToClose: true
        });
      };

      $scope.chiudiFoto = function(ev) {
          $mdDialog.cancel();
      };


      $scope.caricaUltimePosizioni = function(){
        $scope.loadingUltimePosizioni = true;
        api.user.single.ultimePosizioni({id : vm.user.id,start:0,stop:100}).$promise.then(function(r){
            $scope.ultimePosizioni = r;
            $scope.tabellaUltimePosizioni = new NgTableParams({},{dataset : $scope.ultimePosizioni});
            $scope.loadingUltimePosizioni = false;

        });

      }




       $scope.blacklistDevice = function(ev,deviceid){
                    $mdDialog.show({
                       controller: BlackListDeviceController,
                       templateUrl: 'app/main/user/list/blacklist-device-form.html',
                       parent: angular.element(document.body),
                       targetEvent: ev,
                       locals : {
                           item : {
                             uid : vm.user.id,
                             deviceid : deviceid,
                             blacklistdate : Math.round( (new Date()).getTime() / 1000)

                           }
                       },
                       clickOutsideToClose:true,
                       fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                     })
                   .then(function(answer) {
                         getBlacklist();
                   }, function() {
                     $scope.status = 'You cancelled the dialog.';
                   });

      }

      $scope.cancellaDocumento = function(d){
        $scope.driverdata[d]='';
        salvaDriverData();
      }

      function salvaDriverData(){
        api.driverdata.save({id:$scope.driverdata.id},$scope.driverdata,
          function(r){

          })
      }
       $scope.cambiaStatoDocumenti = function(ev){
                    $mdDialog.show({
                       controller: CambiaStatoDocumentiDeviceController,
                       templateUrl: 'app/main/user/list/cambia-stato-documenti.html',
                       parent: angular.element(document.body),
                       targetEvent: ev,
                       locals : {

                             user: vm.user,
                             driverdata : $scope.driverdata



                       },
                       clickOutsideToClose:true,
                       fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                     })
                   .then(function(answer) {
                         getDriverData();
                   }, function() {
                     getDriverData();
                   });

      }
       $scope.blacklistDeviceRimuovi = function(ev,deviceid){
         angular.forEach(vm.listaOfBlacklisted,function(o){
            api.blacklist.elimina({id:o.id},function(){
              getBlacklist();
            });
         });
       }

      $scope.blacklisteddevices = [];


      $scope.caricaViaggiPasseggero = function(){
        api.riderequest.filter({field : "pid",value : vm.user.id}).$promise.then(function(r){
          $scope.viaggiPasseggero = r;
          console.log(r);
          $scope.tabellaViaggiPasseggero = new NgTableParams({},{dataset : $scope.viaggiPasseggero});
        })
      }
      $scope.caricaViaggiDriver = function(){
        api.riderequest.filter({field : "did",value : vm.user.id}).$promise.then(function(r){
          $scope.viaggiDriver = r;
          $scope.tabellaViaggiDriver = new NgTableParams({},{dataset : $scope.viaggiDriver});
        })
      }


      $scope.eliminaUtente = function(){
        api.user.single.delete({id : vm.user.id}).$promise.then(function(r){
          console.log(r);
          $state.go("app.userlist");
        });
      }
      $scope.disassociaUtente = function(){
        vm.user.fbid = '';


           api.user.single.put({id : vm.user.id},vm.user,function(r){
                    $scope.navigazione.mode="edit";
                    $mdDialog.show(
                      $mdDialog.alert()
                        .clickOutsideToClose(true)
                        .parent(angular.element(document.body))
                        .title('Utente salvato')
                        .textContent('Utente salvato correttamente.')
                        .ok('Go')
                    );

           })


      }

      $scope.popupIndirizzo = function(ev,indirizzo){
            $mdDialog.show({
               controller: PopupIndirizzo,
               templateUrl: 'app/main/user/list/popup-indirizzo.html',
               parent: angular.element(document.body),
               targetEvent: ev,
               locals : {
                   item : indirizzo
               },
               clickOutsideToClose:true,
               fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
             })
           .then(function(answer) {
                 getBlacklist();
           }, function() {
             $scope.status = 'You cancelled the dialog.';
           });
      }
      function getBlacklist(){
        api.blacklist.getByDeviceid({id : vm.user.deviceid},
          function(r){



            vm.listaOfBlacklisted = r;
                  vm.deviceInBlacklist =null;
                  angular.forEach(r,function(o){

                    if(o.deviceid == vm.user.deviceid){
                      vm.deviceInBlacklist = o;
                    }
                  });
          }
        );
      }

      function getAddresslist(){
        api.general("address").filter({by : "uid",value:vm.user.id,start:0,stop:100},
          function(r){

            var rr = [null,null];

            angular.forEach(r,function(o){
              if(o.type == 'home')
                rr[0] = o;
              else if(o.type == 'work')
                rr[1] = o
              else
              rr.push(o);
            });

            if(rr[1]==null)
              rr.splice(1,1);
            if(rr[0]==null)
              rr.splice(0,1);


            vm.listaOfAddress = rr;
          }
        );
      }




      var vm = this;
      $scope.navigazione = {
          mode : "edit"
      };

      $scope.attivaModifica = function(){
          $scope.originalUser = angular.copy(vm.user);
          $scope.navigazione.mode = "edit";
      }
      $scope.annullaModifica = function(){
        if( JSON.stringify($scope.originalUser) != JSON.stringify(vm.user)){
                  var confirm = $mdDialog.confirm()
                           .title('Vuoi annullare le modifiche?')
                           .textContent('Tutte le modifiche effettuate andranno perse.')

                           .clickOutsideToClose(true)

                           .ok('Si, annulla le modifiche!')
                           .cancel('No, non ancora');
                     $mdDialog.show(confirm).then(function() {
                       vm.user = angular.copy($scope.originalUser);
                    //  $scope.navigazione.mode = "info";
                     }, function() {

                     });


        }else{
          $scope.navigazione.mode = "info";
        }

      }

      $scope.salvaModifica = function(){
         api.user.single.put({id : vm.user.id},vm.user,function(r){
                  if($scope.driverdata){
                    salvaDriverData();
                  }
                  //$scope.navigazione.mode="info";
                  $mdDialog.show(
                    $mdDialog.alert()
                      .clickOutsideToClose(true)
                      .parent(angular.element(document.body))
                      .title('Utente salvato')
                      .textContent('Utente salvato correttamente.')
                      .ok('Go')
                  );

         })
      }

      $scope.bannaUtente = function(ev){
            $mdDialog.show({
                       controller: BanUserController,
                       templateUrl: 'app/main/user/list/banuser-form.html',
                       parent: angular.element(document.body),
                       targetEvent: ev,
                       locals : {
                           item : vm.user,
                           status : 2
                       },
                       clickOutsideToClose:true,
                       fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                     })
                   .then(function(answer) {
                      vm.user = answer;

                   }, function() {

                   });

      }


      $scope.debannaUtente = function(ev){
            $mdDialog.show({
                       controller: UnBanUserController,
                       templateUrl: 'app/main/user/list/debanuser-form.html',
                       parent: angular.element(document.body),
                       targetEvent: ev,
                       locals : {
                           item : vm.user,
                           status : 1
                       },
                       clickOutsideToClose:true,
                       fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                     })
                   .then(function(answer) {
                      vm.user = answer;

                   }, function() {
                     $scope.status = 'You cancelled the dialog.';
                   });

      }

      vm.referente = null;

      function getDriverData(){
        api.user.single.getDriverData({id:vm.user.id}).$promise.then(function(r){
          if(r.length>0)
            $scope.driverdata = r[0];
          else
            $scope.driverdata = {};
        })
      }
      api.user.single.get($stateParams,
        function(result){
          vm.user = result;

          $scope.caricaViaggiPasseggero();
          $scope.caricaViaggiDriver();
          $scope.caricaUltimePosizioni();


          getListaPromozioni();

           getDriverData();

          if(vm.user.refuid){
              api.user.single.get({id:vm.user.refuid},function(r){
                vm.referente = r;
              })
          }
          console.log(result);
          getBlacklist();
          getAddresslist();
        },
        function(error){
          console.log(error);
        }
      );



      function getListaPin(){
        api.user.pin.get($stateParams,
          function(result){
            vm.pins = result;
          },
          function(error){
            console.log(error);
          }
        );
      }

      getListaPin();



    }








    function BlackListDeviceController($scope,$mdDialog,api,item){
      $scope.titoloDialog = "Blacklist Device";


      if( item ){
        $scope.item = item;

      }




      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){


          api.user.blacklistdevice.post($scope.item,
            function(r){
              $scope.answer(r);
            })
      }


    }



    function CambiaStatoDocumentiDeviceController($scope,$mdDialog,api,user,driverdata){
      $scope.titoloDialog = "Blacklist Device";
      var item;


        $scope.item = angular.copy(driverdata);






      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){


          api.driverdata.save({id:$scope.item.id},$scope.item,
            function(r){
              $scope.answer(r);
            })
      }









 }


})();
