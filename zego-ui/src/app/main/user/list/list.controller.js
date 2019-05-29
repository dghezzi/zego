(function ()
{
    'use strict';

    angular
        .module('app.user.list')
        .controller('UserListController', UserListController)
        .controller('BlackListDeviceController', BlackListDeviceController)
        .controller('BanUserController', BanUserController)
        .controller('UnBanUserController', UnBanUserController)
        .controller('PopupIndirizzo', PopupIndirizzo)
        .controller("NewUserController",NewUserController)
        .controller("CambiaStatoDocumentiDeviceController",CambiaStatoDocumentiDeviceController)
        .controller("EditUserController",EditUserController);
    /** @ngInject */
    function UserListController(api,$scope,$window,NgTableParams,$mdDialog)
    {

      var vm = this;
      vm.window = $window;
    $scope.apibase = api.baseUrl;


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


     $scope.exportAll = function(){



            api.advancedExport({
               entity :'user',
               start  :0,
               stop : 50000
             },$scope.ultimaRicerca,'export-user');
     }




      $scope.resettaFiltri = function(){

        vm.filter = {};
         vm.filter = {
            range :{

            }
          };

        vm.filter.range.insdate = {
          from: new Date("01/01/2014"),
          to : new Date()
        };
        vm.filter.range.lastseen = {
           from: new Date("01/01/2014"),
          to : new Date()
        }

      }

      $scope.resettaFiltri();

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


      $scope.numeroUtenti = 0;
      function getCountAll(){
          api.user.list.count().$promise.then(function(r){
              $scope.numeroUtenti = r.ct;
          });

      }
      getCountAll();


      $scope.numeroDrivers = 0;
      $scope.ultimaRicerca = {};
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
      getCountDrivers();

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
     // getCountRiders();



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
          $scope.tabella.reload();
          $scope.filtriVisibili = false;
        }
        function loadList(){

          $scope.tabella = new NgTableParams({}, {

            getData: function(params) {
             // ajax request to api
              $scope.loadingTable = true;
             var ord = params.orderBy();

             var ricerca = new api.oggettoRicerca();


            if(vm.filter.range && vm.filter.range.insdate){
              vm.filter.range.insdate.from.setHours(0,0,0,0);
              vm.filter.range.insdate.to.setHours(23,59,60,0);
            }
            if(vm.filter.range && vm.filter.range.lastseen){
              vm.filter.range.lastseen.from.setHours(0,0,0,0);
              vm.filter.range.lastseen.to.setHours(23,59,60,0);
            }

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
               entity :'user',
               start  :start,
               stop : stop
             },ricerca).$promise.then(function(data){

                  console.log(data);

                vm.users = data;

                return api.advancedCount.post({
                   entity :'user'
                 },ricerca).$promise.then(function(r){
                   params.total(r.ct);
                    $scope.loadingTable = false;
                   console.log(r);
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




    function NewUserController(api,$stateParams,$scope,$mdDialog,$state,md5)
    {
      var vm = this;
      $scope.navigazione = {
          mode : "new"
      };


      $scope.salva = function(){


          if( $scope.formUser.$valid){


            vm.user.channel = 'backoffice';
            if(vm.user.utype=="admin"){
              if( !vm.user.password || vm.user.password.length < 4){
                alert("Inserisci la password per l'amministratore, deve essere lunga almeno 4 caratteri");
                return;
              }

              vm.user.password = md5.createHash(vm.user.password)

            }else{

            }

            console.log(vm.user)

            api.user.list.filter({
            field: 'email',
            value : vm.user.email
          }).$promise.then(function(o){
            if(o.length == 0 || o[0].id==vm.user.id){


              api.user.list.filter({
                field: 'mobile',
                value : vm.user.mobile
              }).$promise.then(function(o){
                    if(o.length == 0 || o[0].id==vm.user.id){

                        var v = vm.user;

                        if( v.birthdate=undefined ){
                          if( typeof v.birthdate.getMonth === 'function'){
                            v.birthdate = convertDate(v.birthdate.getTime());
                          }
                        }
                      // Tutto bene....salva
                        api.user.single.webcreate(v).$promise.then(function(d){
                            $state.go("app.useredit",{id:d.id});
                        })
                    }else{
                      if(o.length > 1 || o[0].id!=vm.user.id){
                        $mdDialog.show(
                          $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .parent(angular.element(document.body))
                            .title('Errore')
                            .textContent('Il numero di telefono che hai scelto è già associato ad un altro utente.')
                            .ok('Go')
                        );
                      }
                    }
                  });
                }else{
                  console.log(o);
                  if(o.length > 1 || o[0].id!=vm.user.id){
                    $mdDialog.show(
                      $mdDialog.alert()
                        .clickOutsideToClose(true)
                        .parent(angular.element(document.body))
                        .title('Errore')
                        .textContent('La mail che hai scelto è già associata ad un altro utente.')
                        .ok('Go')
                    );
                  }
                }
              });


          }else{
            alert("Controlla che il form sia tutto compilato")

      }
          }
    }



    function EditUserController(api,$stateParams,$scope,$mdDialog,$state,NgTableParams,md5)
    {


   $scope.da = new Date();
      $scope.a = new Date();

      $scope.daf = Math.round($scope.da.getTime() / 1000 );
      $scope.af = Math.round($scope.a.getTime() / 1000 );

      $scope.apibase = api.baseUrl;

      console.log($scope.da,$scope.a);

      $scope.changeDa =function(da){
        console.log('sa',da);
        if(da != undefined){
          $scope.da=da;
          $scope.da.setHours(0, 0, 0, 0);
          $scope.a.setHours(23, 59, 59, 0);
          $scope.daf = Math.round($scope.da.getTime() / 1000 );
          $scope.af = Math.round($scope.a.getTime() / 1000 );

        }
      }

      $scope.changeA =function(a){
         console.log('a');
        if(a != undefined){
          $scope.a=a;
          $scope.da.setHours(0, 0, 0, 0);
          $scope.a.setHours(23, 59, 59, 0);
          $scope.daf = Math.round($scope.da.getTime() / 1000 );
          $scope.af = Math.round($scope.a.getTime() / 1000 );

        }
      }




      $scope.sbloccaGratuitamente = function(){
          api.user.single.sbloccaGratuitamente({},vm.user).$promise.then(function(o){
            vm.user = o;
            $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title('Operazione riuscita')
                .textContent('Debiti sbloccati gratuitamente.')
                .ok('Ok')
            );

          }).catch(function(o){
            $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title('Errore')
                .textContent('Messaggio :'+o.data.msg)
                .ok('Ok')
            );
          });
      }
      $scope.ritentaPagamento = function(){
          api.user.single.ritentaPagamento({},vm.user).$promise.then(function(o){
            vm.user = o;

            $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title('Operazione riuscita')
                .textContent('Il pagamento dei debiti è andato a buon fine.')
                .ok('Ok')
            );

          }).catch(function(o){
            $mdDialog.show(
              $mdDialog.alert()
                .clickOutsideToClose(true)
                .parent(angular.element(document.body))
                .title('Errore')
                .textContent('Messaggio :'+o.data.msg)
                .ok('Ok')
            );
          });;
      }

      $scope.kill = function(){
        api.user.single.kill(vm.user).$promise.then(function(o){

          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Operazione riuscita')
              .textContent('Utente sloggato correttamente.')
              .ok('Ok')
          );


        })
      }
      $scope.fraudReset = function(){
        api.fraud.reset(vm.user).$promise.then(function(o){
          console.log(o);
        })
      }


      $scope.unlockUser = function(){
        var t = vm.user.umode == 'driver' ? 'driver' : 'passeggero';

        var m="Sei sicuro di resettare lo stato del "+t+"? Questa azione lo disconnetterà da eventuali ride in corso.";
         var confirm = $mdDialog.confirm()
                           .title('Resetta stato')
                           .textContent(m)

                           .clickOutsideToClose(true)

                           .ok('Si, procedi!')
                           .cancel('No');
                     $mdDialog.show(confirm).then(function() {

                    api.user.single.unlock({uid : vm.user.id}).$promise.then(function(o){
                            getUser();
                          })

                     }, function() {

                     });




      }


      $scope.getStati = function(){
        api.nation.query().$promise.then(function(d){
            $scope.stati = d;
        })

      }


      $scope.getStati();
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


      $scope.cambiaCanDrive = function(){


        if( !$scope.driverdata || !$scope.driverdata.id){
            vm.user.candrive = !vm.user.candrive ? 1 : 0;
            var driverdata = {
              docok:0,
              uid : vm.user.id,
              status : 1,
              area : 'Milano'

            }

            console.log(driverdata);
            api.driverdata.create(driverdata).$promise.then(function(o){
              $scope.driverdata = o;
              vm.user.candrive = 1;
            })
        }else{
          vm.user.candrive = !vm.user.candrive ? 1 : 0;
        }



      }

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
          //  pid : $scope.nuovapromo.id,
            uid : vm.user.id,
          //  redeemdate : Math.round((new Date()).getTime() / 1000 ),
          //  expirydate : $scope.nuovapromo.validto,
           // valueleft : 0
           code: $scope.nuovapromo.code
          };

          api.promo.userRedeem(userpromo).$promise.then(function(o){
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


       function caricaPagamentiContanti(){
        console.log('caricaPagamentiContanti');
        api.user.single.pagamentiContanti({id : vm.user.id,start:0,stop:100}).$promise.then(function(r){
            $scope.pagamenticontanti = r;
            $scope.tabellaPagamentiContanti = new NgTableParams({},{dataset : $scope.pagamenticontanti});

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

        if($scope.driverdata==undefined || $scope.driverdata.id==undefined){
          return;
        }

        var s = angular.copy($scope.driverdata);


        if($scope.driverdata != undefined && $scope.driverdata.docexpdate != undefined &&
          ( typeof $scope.driverdata.docexpdate.getMonth == 'function' )
        ){
          s.docexpdate = Math.round( s.docexpdate.getTime() / 1000 );
        }



        for (var property in s) {
            if (s.hasOwnProperty(property)) {
              console.log(property);
              console.log(s[property]);
                if(s[property] && typeof s[property].getMonth === 'function'){
                  s[property] = Math.round( s[property].getTime() /1000 );
                }
            }
        }

        if(s.birthcity!= undefined && s.birthcity && s.birthcity.name!=undefined){
          s.birthcity = s.birthcity.name;
        }

        if(s.city!= undefined && s.city && s.city.name!=undefined){
          s.city = s.city.name;
        }

        api.driverdata.save({id:s.id},s,
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

      var filetype;
     var cropper;
     var originalFilename;
     var origfiletype;


     $scope.assicurazioneCambiato = function(t){

       var file = t.files[0];

       if(file.size > 2097152){
         alert("La dimensione massima è 2 mega");

         return;
       }

       api.aws.upload(file).then(function(o){
         console.log(o);

         $scope.driverdata.insuranceimg = o.Location;


         salvaDriverData();
       })
     }
     $scope.patenteCambiato = function(t){

       var file = t.files[0];
       if(file.size > 2097152){
         alert("La dimensione massima è 2 mega");

         return;
       }
       api.aws.upload(file).then(function(o){
         console.log(o);
         $scope.driverdata.docimg = o.Location;
         salvaDriverData();
       })
     }


      $scope.carImgCambiato = function(t){
        var file = t.files[0];

        originalFilename = file.name;
        origfiletype = file.type;
        var answer = file;
        /*$mdDialog.show({
           controller: CropImg,
           templateUrl: 'app/backenduser/crop.html',
           parent: angular.element(document.body),

           locals : {
                file : t.files[0]

           },
           clickOutsideToClose:true,
           fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
         })
       .then(function(answer) {


       */
             api.aws.upload(answer,originalFilename,origfiletype).then(function(o){
               $scope.driverdata.carimg = o.Location;
               salvaDriverData();
             })

      // }, function() {

       //});

      }

      $scope.eliminaFotoAuto = function(){
        console.log($scope.driverdata);
        $scope.driverdata.carimg = '';
        salvaDriverData();

      }

      function convertDate(inputFormat) {
        function pad(s) { return (s < 10) ? '0' + s : s; }
        var d = new Date(inputFormat);
        return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('/');
      }

      vm.newpassword = "";

      $scope.salva = function(){
        $scope.salvaModifica.apply();
      }

      $scope.toggleDriverControl = function (){

            if(!vm.user.bitmask){
              vm.user.bitmask=16;
            }
            else{
              vm.user.bitmask=0;
            }
             $scope.salvaModifica.apply();
      }

      $scope.salvaModifica = function(){




          api.user.list.filter({
            field: 'email',
            value : vm.user.email
          }).$promise.then(function(o){
            if(o.length == 0 || o[0].id==vm.user.id){


              api.user.list.filter({
                field: 'mobile',
                value : vm.user.mobile
              }).$promise.then(function(o){
                if(o.length == 0 || o[0].id==vm.user.id){

                    var v = vm.user;


                    if(v.utype == 'admin' && vm.newpassword != undefined && vm.newpassword!=''){
                      v.password = md5.createHash(vm.newpassword)
                    }

                    if( v.birthdate!=undefined ){
                      if( typeof v.birthdate.getMonth === 'function'){
                        v.birthdate = convertDate(v.birthdate.getTime());
                      }
                    }
                  // Tutto bene....salva
                    api.user.single.put({id : vm.user.id},v,function(r){
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
                             getBan();
                    });
                }else{
                  if(o.length > 1 || o[0].id!=vm.user.id){
                    $mdDialog.show(
                      $mdDialog.alert()
                        .clickOutsideToClose(true)
                        .parent(angular.element(document.body))
                        .title('Errore')
                        .textContent('Il numero di telefono che hai scelto è già associato ad un altro utente.')
                        .ok('Go')
                    );
                  }
                }
              });
            }else{
              console.log(o);
              if(o.length > 1 || o[0].id!=vm.user.id){
                $mdDialog.show(
                  $mdDialog.alert()
                    .clickOutsideToClose(true)
                    .parent(angular.element(document.body))
                    .title('Errore')
                    .textContent('La mail che hai scelto è già associata ad un altro utente.')
                    .ok('Go')
                );
              }
            }
          });
         /*api.user.single.put({id : vm.user.id},vm.user,function(r){
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
                  getBan();
         });
         */

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
                      getBan();
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
                      getBan();
                   }, function() {
                     $scope.status = 'You cancelled the dialog.';
                   });

      }

      vm.referente = null;


      function getBan(){
        api.user.single.getBan({uid:vm.user.id}).$promise.then(function(o){
          if(o.length > 0){
            $scope.banhistory = o[o.length-1];
          }else{
            $scope.banhistory = {};
          }

        })
      }


      function getCalls(){
        api.user.single.getCalls({uid:vm.user.id}).$promise.then(function(o){
           $scope.calls = o;
           $scope.tabellaChiamate = new NgTableParams({},{dataset: $scope.calls});


        })
      }

      function getManufacturer(){
        api.manufacturer.getAll().$promise.then(function(o){
           $scope.listaManufacturer = o;

           console.log(o);

        })
      }

      getManufacturer();


      function getIngaggi(){

        if( vm.user.candrive == 1){
          api.riderequestdrivers.filter({
            field: 'did',
            value : vm.user.id,

          }).$promise.then(function(o){

            $scope.tabellaIngaggi = new NgTableParams({},{dataset:o});
            console.log(o);
          });

        }
      }
      $scope.getCities = function(t){
          return api.cities.query({pfx:t}).$promise;
      }
      $scope.getCitiesRes = function(t){
          return api.cities.query({pfx:t}).$promise;
      }

      function getDriverData(){
        api.user.single.getDriverData({id:vm.user.id}).$promise.then(function(r){
          var dd;
          if(r.length>0){
            if(r[0].docexpdate != undefined ){
              r[0].docexpdate = new Date( r[0].docexpdate * 1000);
            }
              //$scope.driverdata = r[0];
              dd = r[0];
            }else{
              //$scope.driverdata = {};
              dd = {};
            }

            if(dd.insdate != undefined && dd.insdate ){
              dd.insdate = new Date(dd.insdate * 1000);
            }

            if(dd.moddate != undefined && dd.moddate ){
              dd.moddate = new Date(dd.moddate * 1000);
            }

            /*if(dd.docexpdate != undefined && dd.docexpdate ){
              dd.docexpdate = new Date(dd.docexpdate * 1000);
            }*/

            if(dd.insuranceexpdate != undefined && dd.insuranceexpdate ){
              dd.insuranceexpdate = new Date(dd.insuranceexpdate * 1000);
            }

            $scope.driverdata = dd;
            console.log(dd);
        })
      }



      function getUser(){
        api.user.single.get($stateParams,
        function(result){

          if(result.birthdate != undefined && result.birthdate ){
            try {
              var dateString = result.birthdate; // Oct 23
              var dateParts = dateString.split("/");
              result.birthdate = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
            } catch (e) {

              result.birthdate = new Date(result.birthdate * 1000);
            } finally {

            }

            //result.birthdate = new Date(result.birthdate);
          }


          vm.user = result;

          $scope.caricaViaggiPasseggero();
          $scope.caricaViaggiDriver();
          $scope.caricaUltimePosizioni();
          caricaPagamentiContanti();
          getBan();
          getListaPromozioni();
          getCalls();


          getIngaggi();

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
      }

      getUser();

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


      function salvaDriverData(){

        $scope.driverdata = $scope.item;

        var s = angular.copy($scope.driverdata);


        if($scope.driverdata != undefined && $scope.driverdata.docexpdate != undefined &&
          ( typeof $scope.driverdata.docexpdate.getMonth == 'function' )
        ){
          s.docexpdate = Math.round( s.docexpdate.getTime() / 1000 );
        }



        for (var property in s) {
            if (s.hasOwnProperty(property)) {
              console.log(property);
              console.log(s[property]);
                if(s[property] && typeof s[property].getMonth === 'function'){
                  s[property] = Math.round( s[property].getTime() /1000 );
                }
            }
        }

        if(s.birthcity!= undefined && s.birthcity && s.birthcity.name!=undefined){
          s.birthcity = s.birthcity.name;
        }

        if(s.city!= undefined && s.city && s.city.name!=undefined){
          s.city = s.city.name;
        }

        api.driverdata.save({id:s.id},s,
          function(r){
            $scope.answer(r);
          })
      }

      $scope.salva = function(){

          salvaDriverData();
          /*api.driverdata.save({id:$scope.item.id},$scope.item,
            function(r){
              $scope.answer(r);
            })*/
          }


    }

    function BanUserController($scope,$mdDialog,api,item,status){
      $scope.titoloDialog = "Banna utente";


      $scope.motivazioniBan =
      [
        {
          v : 1,
          label : "Frode carta di credito"
        },
        {
          v : 2 ,
          label : "Comportamento non conforme"
        },
        {
          v : 3,
          label: "Profilo fittizio"
        },
        {
          v:4,
          label : "Altro"
        }

      ];
      $scope.item = item;



      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.salva = function(){
          var r =angular.copy($scope.item)
          //r.status = status;

          r.banreason = $scope.banreason;

          api.user.single.ban(r,
            function(r){
              $scope.answer(r);
            })
      }


    }





    function UnBanUserController($scope,$mdDialog,api,item,status){
          $scope.titoloDialog = "Banna utente";


          $scope.item = item;



          $scope.cancel = function() {
            $mdDialog.cancel();
          };
          $scope.answer = function(answer) {
            $mdDialog.hide(answer);
          };


          $scope.salva = function(){
              var r =angular.copy($scope.item)


              api.user.single.unban(r,
                function(r){
                  $scope.answer(r);
                })
          }


        }





        function PopupIndirizzo($scope,$mdDialog,api,item){
              $scope.titoloDialog = item.address;


              $scope.item = item;

              $scope.map = {
                center: {
                  latitude: item.lat,
                  longitude: item.lng},
                zoom: 8
              };


              $scope.cancel = function() {
                $mdDialog.cancel();
              };
              $scope.answer = function(answer) {
                $mdDialog.hide(answer);
              };

      }



})();
