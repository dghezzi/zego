(function ()
{
    'use strict';

    angular
        .module('app.riderequest.list')
        .controller('RiderequestListController', RiderequestListController)
        .controller('RefundSingleController', RefundSingleController)
        .controller('EditaDatiPagamentoController', EditaDatiPagamentoController)
        .controller('NuovoPagamentoController', NuovoPagamentoController)

        .controller("EditRiderequestController",EditRiderequestController);



    /** @ngInject */




    function RefundSingleController($scope,$mdDialog,api,item,ride){
      $scope.titoloDialog = "Refund";
      $scope.ride = ride;
      $scope.item = {
        amount : item.amount/100
      };

      $scope.payment = item;




      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };




      $scope.$watch("item.amount",function(){
        var i = $scope.item.amount;
        console.log(i);
        i= i.toString().replace(",",".");
        i = i*100;

        if(i > $scope.payment.amount){
          var ii=$scope.payment.amount;
          $scope.item.amount = ii /100;
        }

      })

      $scope.salva = function(){
        var item = angular.copy($scope.item);




        item.rid = $scope.ride.id;
        item.uid = $scope.ride.pid;
        item.pid = $scope.payment.id;
        item.action = "refund";

        item.amount = item.amount.toString();
        item.amount = item.amount.replace(",",".");
        item.amount = Math.round(item.amount*100);

          api.payment.other(item,
            function(r){
              $scope.answer(r);
            })
      }


    }





    function EditaDatiPagamentoController($scope,$mdDialog,api,ride){
      $scope.titoloDialog = "Refund";
      //$scope.ride = ride;


    $scope.item = angular.copy(ride);

    //driverfee,zegofee,stripedriverfee,stripezegofee,extprice,passprice,driverprice,discount
      $scope.item.driverfee = ($scope.item.driverfee / 100).toFixed(2);
      $scope.item.zegofee = ($scope.item.zegofee / 100).toFixed(2);
      $scope.item.stripedriverfee = ($scope.item.stripedriverfee / 100).toFixed(2);
      $scope.item.stripezegofee = ($scope.item.stripezegofee / 100).toFixed(2);
      $scope.item.extprice = ($scope.item.extprice / 100).toFixed(2);
      $scope.item.passprice = ($scope.item.passprice / 100).toFixed(2);
      $scope.item.driverprice = ($scope.item.driverprice / 100).toFixed(2);
      $scope.item.discount = ($scope.item.discount / 100).toFixed(2);



      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.$watch("item.amount",function(){



      })

      $scope.salva = function(){



        var item = angular.copy($scope.item);

        item.driverfee=item.driverfee.replace(",",".");
        item.zegofee=item.zegofee.replace(",",".");
        item.stripedriverfee=item.stripedriverfee.replace(",",".");
        item.stripezegofee=item.stripezegofee.replace(",",".");
        item.extprice=item.extprice.replace(",",".");
        item.passprice=item.passprice.replace(",",".");
        item.driverprice=item.driverprice.replace(",",".");
        item.discount=item.discount.replace(",",".");


        item.driverfee = Math.round(item.driverfee *100);
        item.zegofee = Math.round(item.zegofee *100);
        item.stripedriverfee = Math.round(item.stripedriverfee *100);
        item.stripezegofee = Math.round(item.stripezegofee *100);
        item.extprice = Math.round(item.extprice *100);
        item.passprice = Math.round(item.passprice *100);
        item.driverprice = Math.round(item.driverprice *100);
        item.discount = Math.round(item.discount *100);



        api.riderequest.put({id : item.id},item).$promise.then(function(o){
          $scope.answer(o);

        })

      }


    }






    function NuovoPagamentoController($scope,$mdDialog,api,ride){
      $scope.titoloDialog = "Refund";
      $scope.ride = ride;
      $scope.item = {
        amount : 0
      };





      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };


      $scope.$watch("item.amount",function(){


      })

      $scope.salva = function(){
        var item = angular.copy($scope.item);


        item.rid = $scope.ride.id;
        item.uid = $scope.ride.pid;

        item.action = "newpayment";

        item.amount = item.amount.replace(",",".");
        item.amount = Math.round(item.amount*100);



          api.payment.other(item,
            function(r){
              $scope.answer(r);
            })
      }


    }

    function RiderequestListController(api,$scope,$window,NgTableParams)
    {

      var vm = this;
      vm.window = $window;



      $scope.resettaFiltri = function(){
        vm.filter = {
          range :{
            reqdate : {}
          }
        };

        vm.filter.range.reqdate.from = new Date( (new Date()).getTime() - (1000 * 3600 * 24 *7 )  ),
        vm.filter.range.reqdate.to = new Date();
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


       $scope.exportAll = function(){
            api.advancedExport({
               entity :'riderequest',
               start  :0,
               stop : 50000
             },$scope.ultimaRicerca,'export-riderequest');

     }


      $scope.requestStatus = api.requestStatus;
      $scope.requestStatusLabel = api.requestStatusLabel;
      $scope.requestStatusLabelFilter = api.requestStatusLabelFilter;



      $scope.ultimaRicerca={};

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
             console.log(params.page());
             console.log(params.count());
              var ord = params.orderBy();
             var ricerca = new api.oggettoRicerca();




              if(vm.filter.range && vm.filter.range.reqdate){
              vm.filter.range.reqdate.from.setHours(0,0,0,0);
              vm.filter.range.reqdate.to.setHours(23,59,60,0);
            }



             ricerca.buildFilters(vm.filter);

             if( ord.length > 0 ){
               vm.filter.order = ord[0].substr(0,1) == '+' ? "desc" : "asc";
               vm.filter.orderby = ord[0].substr(1,ord[0].length-1);
               ricerca.orderBy(vm.filter.orderby,vm.filter.order);
             }else{
               ricerca.orderBy("id","DESC");
             }

             console.log(ricerca);
             var start = (params.page() - 1) * params.count();
             var stop = params.page() * params.count();

             $scope.ultimaRicerca = ricerca;
             return  api.advancedSearch.post({
                entity :'riderequest',
                start  :start,
                stop : stop
              },ricerca).$promise.then(function(data){
                   $scope.list = data;

                 return api.advancedCount.post({
                    entity :'riderequest'
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




             return api.riderequest.pag(
               {
                 page : params.page() - 1,
                 start : start,
                 stop : stop,
                 num : params.count()
               }
             ).$promise.then(function(data) {
               //params.total(500); // recal. page nav controls
                  console.log(data);
                    $scope.list = data;
                   return api.riderequest.count().$promise.then(function(r){

                     params.total(r.ct);

                     return data;
                   })

             });
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


    function EditRiderequestController(api,$stateParams,$scope,$mdDialog,$state,uiGmapIsReady,NgTableParams)
    {


      $scope.notificationStatus =
      {
        "0" : "SCHEDULED",
        '1' : "SENT",
        "2" : "CANCELED"
      };

      $scope.annullaViaggio = function(){

          var confirm = $mdDialog.confirm()
              .title('Sicuro di annullare il viaggio?')
              .textContent('Verrà effettuato un refund completo.')
              .targetEvent()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .ok('Si, procedi!')
              .cancel('No');
        $mdDialog.show(confirm).then(function() {


                    var p = {
                      action : 'refund',
                      uid : $scope.riderequest.pid,
                      rid : $scope.riderequest.id
                    };
                    api.payment.main(p).$promise.then(function(o){
                       $mdDialog.show(
                                $mdDialog.alert()
                                  .clickOutsideToClose(true)
                                  .parent(angular.element(document.body))
                                  .title('Salvato')
                                  .textContent('Viaggio annullato.')
                                  .ok('Go')
                              );

                      $scope.riderequest = o;
                    })

        }, function() {

        });






      }
      $scope.dettaglioPagamentoAperto=null;
      $scope.toggleDettaglioPagamento = function(p){
          if($scope.dettaglioPagamentoAperto == p.id){
            $scope.dettaglioPagamentoAperto=null;
          }else{
            $scope.dettaglioPagamentoAperto=p.id;
          }
      }
      $scope.map = { center: { latitude: 44, longitude: 11 }, zoom: 8 };

      $scope.requestStatus = api.requestStatus;
      $scope.requestStatusLabel = api.requestStatusLabel;


      $scope.requestDriverStatus = api.requestDriverStatus;

      var vm = this;
      $scope.navigazione = {
          mode : "edit"
      };

      function getPayments(){
        api.payment.ride({rid:$scope.riderequest.id}).$promise.then(function(r){
          $scope.payments = r;
          $scope.paymentsActions = [];

          _.each($scope.payments,function(o){

           _.each(o.actions,function(a){

            $scope.paymentsActions.push(a);

           });


       });

          $scope.tabellaPagamenti = new NgTableParams({},{dataset:$scope.paymentsActions});
          console.log(r);
        })
      }

      /* PAGAMENTI */
      $scope.capture = function(){
        var p = {
          action : 'capture',
          uid : $scope.riderequest.pid,
          rid : $scope.riderequest.id
        };
        api.payment.main(p).$promise.then(function(o){
          $scope.riderequest = o;
        })
      }
      $scope.refund = function(){
        var p = {
          action : 'refund',
          uid : $scope.riderequest.pid,
          rid : $scope.riderequest.id
        };
        api.payment.main(p).$promise.then(function(o){
          $scope.riderequest = o;
        })
      }

      $scope.refundSingle = function(p){
            $mdDialog.show({
               controller: RefundSingleController,
               templateUrl: 'app/main/riderequest/list/refund-single.html',
               parent: angular.element(document.body),

               locals : {
                   item : p,
                   ride : $scope.riderequest
               },
               clickOutsideToClose:true,
               fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
             })
           .then(function(answer) {
             getPayments();
           }, function() {
             $scope.status = 'You cancelled the dialog.';
           });

      }

      $scope.creaNuovoPagamento = function(p){
            $mdDialog.show({
               controller: NuovoPagamentoController,
               templateUrl: 'app/main/riderequest/list/nuovo-pagamento.html',
               parent: angular.element(document.body),

               locals : {

                   ride : $scope.riderequest
               },
               clickOutsideToClose:true,
               fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
             })
           .then(function(answer) {
             getPayments();
           }, function() {
             $scope.status = 'You cancelled the dialog.';
           });

      }





      $scope.editaDatiPagamento = function(p){
            $mdDialog.show({
               controller: EditaDatiPagamentoController,
               templateUrl: 'app/main/riderequest/list/popup-edita.html',
               parent: angular.element(document.body),

               locals : {

                   ride : $scope.riderequest
               },
               clickOutsideToClose:true,
               fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
             })
           .then(function(answer) {
             $scope.riderequest = answer;
           }, function() {
             $scope.status = 'You cancelled the dialog.';
           });

      }




      $scope.salva = function(){
         api.riderequest.put({id : $scope.riderequest.id},$scope.riderequest,function(r){
                  $scope.riderequest = r;

                  $mdDialog.show(
                    $mdDialog.alert()
                      .clickOutsideToClose(true)
                      .parent(angular.element(document.body))
                      .title('Salvato')
                      .textContent('Riderequest aggiornata.')
                      .ok('Go')
                  );

         })
      }


      vm.referente = null;

      var map;
      $scope.mapDirection=null;


      $scope.updateRating = function(type,newr){


        api.riderequest.updaterating({
          id : $scope.riderequest.id
        },{
          type : type,
          newrating : newr
        }).$promise.then(function(o){

          $scope.riderequest = o;

        })
      }

      $scope.getDrivers = function(){
        api.riderequest.getDrivers({id:$scope.riderequest.id}).$promise.then(function(r){
            $scope.drivers = r;
            $scope.tabellaDrivers = new NgTableParams({},{dataset: r});

        })
      }


      function getUseraction(){
         api.riderequest.useraction({id : $scope.riderequest.id}).$promise.then(function(r){

              $scope.tabellaUseraction = new NgTableParams({},{dataset: r});

              $scope.listaUseraction = r;
          })
       }

       function getNotifications(){
         api.riderequest.notifications({id : $scope.riderequest.id}).$promise.then(function(r){

              $scope.tabellaNotifications = new NgTableParams({},{dataset: r});

              $scope.listaNotifications = r;
          })
       }

      uiGmapIsReady.promise(1).then(function(instances) {
          instances.forEach(function(inst) {
              map = inst.map;


              api.riderequest.get($stateParams,
                function(result){


                  $scope.riderequest = result;


                  api.riderequest.feedback({id:$scope.riderequest.id}).$promise.then(function(p){
                    angular.forEach(p,function(o){
                      if($scope.riderequest.pid!=undefined && o.sender == $scope.riderequest.pid ){
                        $scope.passengerFeedback = o;
                      }
                      if($scope.riderequest.did!=undefined && o.sender == $scope.riderequest.did ){
                        $scope.driverFeedback = o;
                      }
                    })
                  })
                  $scope.userpromoApplicata=null;


                  if( $scope.riderequest.promoid!=undefined  ){
                    api.userpromo.get({id : $scope.riderequest.promoid}).$promise.then(function(r){
                      $scope.userpromoApplicata = r;
                    })
                  }



                  getUseraction();
                  getNotifications();


                  if(result.drivrating && result.drivrating < 3){

                  }
                  if( result.promoid!= undefined ){
                      api.promo.get({id : result.promoid}).$promise.then(function(o){
                        $scope.promo = o;
                      })
                  }
                    getPayments();
                    $scope.getDrivers();

                  api.user.single.get({id: $scope.riderequest.pid}).$promise.then(function(r){
                      $scope.passenger = r;
                  })
                  if($scope.riderequest.did){
                    api.user.single.get({id: $scope.riderequest.did}).$promise.then(function(r){
                        $scope.driver = r;
                    })
                  }


                  if($scope.riderequest.pulat ){
                        var bounds = new google.maps.LatLngBounds();
                        var markerStart = new google.maps.Marker({
                           map: map,
                           draggable: false,
                           position: new google.maps.LatLng({lat: $scope.riderequest.pulat, lng: $scope.riderequest.pulng}),
                           icon: {
                               url : '/assets/images/flag_icon.png',
                               size : new google.maps.Size(30, 45),
                               scaledSize: new google.maps.Size(30, 45)
                           }
                         });
                         bounds.extend(new google.maps.LatLng({lat: $scope.riderequest.pulat, lng: $scope.riderequest.pulng}));

                         if( $scope.riderequest.dolat){
                               var markerEnd = new google.maps.Marker({
                                  map: map,
                                  draggable: false,
                                  position: new google.maps.LatLng({lat: $scope.riderequest.dolat, lng: $scope.riderequest.dolng}),
                                  icon: {
                                      url : '/assets/images/flag_icon.png',
                                      size : new google.maps.Size(30, 45),
                                      scaledSize: new google.maps.Size(30, 45)
                                  }
                                });
                                bounds.extend(new google.maps.LatLng({lat: $scope.riderequest.dolat, lng: $scope.riderequest.dolng}));
                          }

                         map.fitBounds(bounds);
                  }


                  if($scope.riderequest.realpulat ){
                        var bounds = new google.maps.LatLngBounds();
                        var markerStart = new google.maps.Marker({
                           map: map,
                           draggable: false,
                           position: new google.maps.LatLng({lat: $scope.riderequest.realpulat, lng: $scope.riderequest.realpulng}),
                           icon: {
                               url : '/assets/images/pointer.png',
                               size : new google.maps.Size(30, 45),
                               scaledSize: new google.maps.Size(30, 45)
                           }
                         });
                         bounds.extend(new google.maps.LatLng({lat: $scope.riderequest.realpulat, lng: $scope.riderequest.realpulng}));

                         if( $scope.riderequest.realdolat){
                               var markerEnd = new google.maps.Marker({
                                  map: map,
                                  draggable: false,
                                  position: new google.maps.LatLng({lat: $scope.riderequest.realdolat, lng: $scope.riderequest.realdolng}),
                                  icon: {
                                      url : '/assets/images/pointer.png',
                                      size : new google.maps.Size(30, 45),
                                      scaledSize: new google.maps.Size(30, 45)
                                  }
                                });
                                bounds.extend(new google.maps.LatLng({lat: $scope.riderequest.realdolat, lng: $scope.riderequest.realdolng}));

                                var start = new google.maps.LatLng({lat: $scope.riderequest.realpulat, lng: $scope.riderequest.realpulng});
                                var end = new google.maps.LatLng({lat: $scope.riderequest.realdolat, lng: $scope.riderequest.realdolng});
                                var request = {
                                  origin: start,
                                  destination: end,
                                  travelMode: google.maps.TravelMode.DRIVING
                                };


                                var directionsService = new google.maps.DirectionsService();
                                $scope.mapDirection = new google.maps.DirectionsRenderer({suppressMarkers: true,polylineOptions : {strokeColor: "#ff0000"}});
                                $scope.mapDirection.setMap(map);

                                directionsService.route(request, function(result, status) {

                                    if (status == google.maps.DirectionsStatus.OK) {

                                      $scope.mapDirection.setDirections(result);
                                    } else {
                                      alert("couldn't get directions:" + status);
                                    }
                                });

                          }

                         map.fitBounds(bounds);
                  }


                },
                function(error){
                  console.log(error);
                }
              );
          });

      });


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
