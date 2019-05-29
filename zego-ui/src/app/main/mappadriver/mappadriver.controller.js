(function ()
{
  'use strict';

  angular
  .module('app.mappadriver')

  .controller("MappaDriverController",MappaDriverController);

  /** @ngInject */
  function  MappaDriverController(api,$stateParams,$scope,$mdDialog,$state,uiGmapIsReady,$filter,$mdpTimePicker,$interval)
  {
    $scope.map = { center: { latitude: 44, longitude: 11 }, zoom: 8 };



    $scope.filtroDriver = 'tutti';
    $scope.filtra = function(f){
      $scope.filtroDriver = f;
    }
    $scope.requestStatus = api.requestStatus;
    $scope.requestStatusLabel = api.requestStatusLabel;


    var vm = this;

    var map;

    function formattaData(){

    }
    $scope.filtraIntervallo = function(){

      vm.filter = angular.copy(vm.intervallo.filter);

      console.log(vm.intervallo);
      $scope.filtermode = "Dal " + $filter("date")(vm.intervallo.filter.range.reqdate.from,'yyyy-MM-dd')+" al "+$filter("date")(vm.intervallo.filter.range.reqdate.to,'yyyy-MM-dd');

      getListaRideRequests();
    }

    $scope.filtraDataSingola= function(){

      /*vm.filter = angular.copy(vm.datasingola.filter);
        $scope.filtermode = "Nella giornata di "+$filter("date")(vm.datasingola.filter.range.reqdate.from,'yyyy-MM-dd');
      var d = vm.filter.range.reqdate.from;
      d.setHours(0,0,0,0);
      var from = Math.round(d.getTime() / 1000);
      d.setHours(23,59,0,0);
      var to = Math.round(d.getTime() / 1000);*/

    /*  console.log(vm.orainizio);
      var from = Math.round(vm.orainizio.getTime() / 1000);

      var to = Math.round(vm.orafine.getTime() / 1000);
      */
      vm.filter = {range : {}};
      vm.filter.range.reqdate =
      {
        from : vm.orainizio,
        to: vm.orafine
      }
      getListaRideRequests();
    }

    $scope.heatmapLayer =null;
    vm.orainizio = new Date().setHours(0,0,0,0)
    vm.orafine = new Date().setHours(23,59,59,99)


    $scope.$watch("vm.datasingola",function(){
      var d = angular.copy(vm.datasingola);
      if(d===undefined)
      return;
      vm.orainizio = d.setHours(0,0,0,0)
      vm.orafine = d.setHours(23,59,59,99)
    })
    $scope.pickOrainizio = function(ev) {
    	$mdpTimePicker(vm.orainizio, {
        targetEvent: ev
      }).then(function(selectedDate) {
        console.log(selectedDate);
        vm.orainizio = selectedDate;
      });;
    }

    window.vaiSchedaUtente = function(id){

        var url = $state.href('app.useredit', {id: id});
        window.open(url,'_blank');
    }


    $scope.markerSuMappa = [];

    $scope.pickOrafine = function(ev) {
      $mdpTimePicker(vm.orafine, {
        targetEvent: ev
      }).then(function(selectedDate) {
        console.log(selectedDate);
        vm.orafine = selectedDate;
      });;
    }



          $scope.rtstatus = [];

         $scope.rtstatus['100'] = 'PASSENGER_IDLE';
         $scope.rtstatus['101'] = 'PASSENGER_REQUEST_SENT';
         $scope.rtstatus['102'] = 'PASSENGER_WAITING_DRIVER';
         $scope.rtstatus['103'] = 'PASSENGER_ONRIDE';
         $scope.rtstatus['104'] = 'PASSENGER_PAYMENT_DUE';
         $scope.rtstatus['105'] = 'PASSENGER_FEEDBACK_DUE';

         $scope.rtstatus['200'] = 'DRIVER_IDLE';
         $scope.rtstatus['201'] = 'DRIVER_ANSWERING';
         $scope.rtstatus['202'] = 'DRIVER_PICKINGUP';
         $scope.rtstatus['203'] = 'DRIVER_ONRIDE';
         $scope.rtstatus['204'] = 'DRIVER_FEEDBACKDUE';




         function renderDriverMappa(r){

            angular.forEach($scope.markerSuMappa,function(o){
                o.setMap(null);
            });
                     var punti = [];

                     angular.forEach($scope.riderequests,function(r){
                       var icon = 'driver_grigio.png' ;

                       if(r.status == 201){
                         icon = 'driver_blu.png';
                       }
                       if(r.status == 202){
                         icon = 'driver_blu.png';
                       }
                       if(r.status == 203){
                         icon = 'standard_icon.png';
                       }
                       if(r.status == 204){
                         icon = 'driver_nero.png';
                       }
                      var bounds = new google.maps.LatLngBounds();
                       var markerStart = new google.maps.Marker({
                         map: map,
                         draggable: false,
                         position: new google.maps.LatLng({lat: r.lat, lng: r.lng}),
                         icon: {
                             url : '/assets/images/'+icon,
                             size : new google.maps.Size(30, 30),
                             scaledSize: new google.maps.Size(30, 30)
                         }
                       });





                       (function(marker,ride){
                         var d = $filter('date')(new Date(ride.llocdate * 1000) ,'medium' );
                         var contentString = '<div><b>'+ride.fname+" "+ride.lname+'</b></div><div>'+d+'</div><div>'+$scope.rtstatus[ride.status]+'</div><br><a onclick="vaiSchedaUtente('+ride.did+')">Scheda driver</a>';
                         var infowindow = new google.maps.InfoWindow({
                           content: contentString
                         });
                         marker.addListener('click', function() {
                          infowindow.open(map, marker);
                        });
                      })(markerStart,r);

                        $scope.markerSuMappa.push(markerStart);


                       punti.push( new google.maps.LatLng({lat: r.lat, lng: r.lng}) );
                       //bounds.extend(new google.maps.LatLng({lat: r.realpulat, lng: r.realpulng}));
                     });

                      
                      google.maps.event.trigger(map, "resize");

         }

         $scope.Timer = $interval(function(){
           getListaRideRequests()
         },10000);


    $scope.$on("$destroy",function(){
          if (angular.isDefined($scope.Timer)) {
              $interval.cancel($scope.Timer);
          }
      });

    function getListaRideRequests(){

      var ricerca = new api.oggettoRicerca(vm.filter);


      if( vm && vm.generic && vm.generic.simple && vm.generic.simple.status!=undefined){

        ricerca.addField("status",vm.generic.simple.status);
      }

  /*    api.riderequest.list({},
        function(result){*/
        /*api.advancedSearch.post({
           entity :'riderequest',
           start  :0,
           stop : 1000000
         },ricerca).$promise.then(function(result){*/

         api.user.list.drivingnow().$promise.then(function(result){

          $scope.riderequests = result;

          renderDriverMappa($scope.riderequests);

          /*if( $scope.heatmapLayer!=null ){
            $scope.heatmapLayer.setMap(null);
          }
          $scope.heatmapLayer = new google.maps.visualization.HeatmapLayer({
            data: punti,
            map: map
          }); 
          */
        },
        function(error){
          console.log(error);
        }
      );

    }

    uiGmapIsReady.promise(1).then(function(instances) {
      instances.forEach(function(inst) {
        map = inst.map;
         
          
        getListaRideRequests();

      });


    });


  }


})();
