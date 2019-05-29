(function ()
{
    'use strict';

    angular
        .module('app.mappadriver', ['ngTable'])
        .config(config);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider,msNavigationServiceProvider)
    {


      // State
      $stateProvider
      .state('app.mappadriver', {
          url    : '/mappadriver',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/mappadriver/mappadriver.html',
                  controller : 'MappaDriverController as vm'
              }
          }

      });



      msNavigationServiceProvider.saveItem('users.mappadriver', {
          title    : 'Driver Map',
          icon     : 'icon-map-marker-radius',
          state    : 'app.mappadriver',

          weight   : 4
      });

      /*
      uiGmapGoogleMapApiProvider.configure({
          //    key: 'your api key',
          v: '3.25', //defaults to latest 3.X anyhow
          libraries: 'visualization'
      });

        // Navigation
      /*  msNavigationServiceProvider.saveItem('pages', {
            title : 'PAGES',
            group : true,
            weight: 2
        });
        */
    }
})();
