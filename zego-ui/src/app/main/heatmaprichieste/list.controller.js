(function ()
{
  'use strict';

  angular
  .module('app.heatmap')

  .controller("HeatmapController",HeatmapController);

  /** @ngInject */
  function  HeatmapController(api,$stateParams,$scope,$mdDialog,$state,uiGmapIsReady,$filter,$mdpTimePicker)
  {
    $scope.map = { center: { latitude: 45.272398, longitude: 8.389686 }, zoom: 9 , options: {scrollwheel : false}};




    $scope.requestStatus = api.requestStatus;
    $scope.requestStatusLabel = api.requestStatusLabel;

$scope.requestStatusLabel[0]='TUTTI';

    var vm = this;
    vm.intervallo = {
      filter : {
        range : {
          reqdate:{}
        }
      }
    }

    vm.generic={
      simple : {
        status : 0
      }
    }


  vm.intervallo.filter.range.reqdate.to=moment().toDate();
   vm.intervallo.filter.range.reqdate.from =moment().clone().subtract(3,'months').toDate();
    var map;

    function formattaData(){

    }
    $scope.filtraIntervallo = function(){


      vm.filter = angular.copy(vm.intervallo.filter);

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
    vm.orainizio = new Date();
    vm.orainizio.setHours(0,0,0,0);
    vm.orafine = new Date()
    vm.orafine.setHours(23,59,59,99);


    $scope.$watch("vm.datasingola",function(){
      var d = angular.copy(vm.datasingola);
      if(d===undefined)
      return;
      vm.orainizio = d;
      vm.orainizio.setHours(0,0,0,0);

      vm.orafine = d
      vm.orafine.setHours(23,59,59,99)
    })
    $scope.pickOrainizio = function(ev) {
    	$mdpTimePicker(vm.orainizio, {
        targetEvent: ev
      }).then(function(selectedDate) {
        vm.orainizio = selectedDate;
      });;
    }
    $scope.pickOrafine = function(ev) {
      $mdpTimePicker(vm.orafine, {
        targetEvent: ev
      }).then(function(selectedDate) {
        vm.orafine = selectedDate;
      });;
    }



    function getListaRideRequests(){

     // var ricerca = new api.oggettoRicerca(vm.filter);


      /*if( vm && vm.generic && vm.generic.simple && vm.generic.simple.status!=undefined){

        ricerca.addField("status",vm.generic.simple.status);
      }*/

  /*    api.riderequest.list({},
        function(result){*/


          api.heatmap.get({
            start: Math.round( vm.filter.range.reqdate.from.getTime() /1000 ),
            stop : Math.round( vm.filter.range.reqdate.to.getTime() / 1000 ),
            stato : vm.generic.simple.status
          }).$promise.then(function(o){


              var punti = [];
            _.each(o,function(r){
            punti.push( new google.maps.LatLng({lat: r.l, lng: r.n}) );

          });
          if( $scope.heatmapLayer!=null ){
            $scope.heatmapLayer.setMap(null);

          }
          $scope.heatmapLayer = new google.maps.visualization.HeatmapLayer({
            data: punti,//getPoints(),
            map: map,
            opacity: 0.5,
            maxIntensity: 8,
            radius: 8
          });




          })
          return;








    }

    uiGmapIsReady.promise(1).then(function(instances) {
      instances.forEach(function(inst) {
        map = inst.map;


        $scope.filtraIntervallo();

      });

    });


  }


})();
