(function ()
{
    'use strict';

    angular
        .module('fuse')
        .run(runBlock);

    /** @ngInject */
    function runBlock($rootScope, $timeout, $state,api,msNavigationService,$location)
    {

      var g = window.location.search.substr(1);
      if( g.substring(0,10) == 'zegotoken='){
        var result = null,
            tmp = [];
        window.location.search
        .substr(1)
            .split("&")
            .forEach(function (item) {
            tmp = item.split("=");
            if (tmp[0] === 'zegotoken') result = decodeURIComponent(tmp[1]);
        });

        api.user.list.filter({field:"zegotoken",value:result}).$promise.then(function(o){
          if(o && o.length && o[0].id){

              api.saveLoginPersistent(o[0]);
              $rootScope.userLogged = o;
              $location.path("/");
          }
        })
      }

      $rootScope.version = api.versione.corrente();

        // Activate loading indicator
        var stateChangeStartEvent = $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
       // console.log('$stateChangeStart',toState, toParams, fromState, fromParams);

            $rootScope.loadingProgress = true;
            api.checkLoginPersistent();

            if( !$rootScope.userLogged && toState.name!="app.login" && toState.name!="app.adminlogin" ){

                event.preventDefault();
                return $state.go("app.login");

            }


            if($rootScope.userLogged){
              if($rootScope.userLogged.utype != 'admin'){
                  msNavigationService.deleteItem("impostazioni");
                  msNavigationService.deleteItem("impostazioni.app");
                  msNavigationService.deleteItem("impostazioni.messaggi");
                  msNavigationService.deleteItem("zoneservizi");
                  msNavigationService.deleteItem("users");
                  msNavigationService.deleteItem("blacklistdevicelist");
                  msNavigationService.deleteItem("promo");
                  msNavigationService.deleteItem("users.driver");
                  msNavigationService.deleteItem("report");

              }

              if( $rootScope.userLogged.utype!="admin"
              && toState.name!="app.mydashboard"
              && toState.name!="app.myviaggi"
              && toState.name!="app.logout" ){
                  event.preventDefault();
                  return $state.go("app.mydashboard");
              }
            }



        });

        // De-activate loading indicator
        var stateChangeSuccessEvent = $rootScope.$on('$stateChangeSuccess', function ()
        {
            $timeout(function ()
            {
                $rootScope.loadingProgress = false;
            });
        });

        // Store state in the root scope for easy access
        $rootScope.state = $state;

        // Cleanup
        $rootScope.$on('$destroy', function ()
        {
            stateChangeStartEvent();
            stateChangeSuccessEvent();
        });
    }

    angular.module('ngTable').run(['$templateCache', function ($templateCache) {
      $templateCache.put('ng-table/filters/text.html', '<input type="text" ng-model="params.filter()[name]" ng-if="filter==\'text\'" placeholder="Ricerca campo : {{name}}" class="input-filter form-control"/>');
    }]);

    angular.module('utils',[]).factory('AuthService', function ( $rootScope) {

      var returnObj={};

      returnObj.isAdmin = function () {

        return $rootScope.userLogged.utype=="admin";
      };


      return returnObj;
    });
})();
