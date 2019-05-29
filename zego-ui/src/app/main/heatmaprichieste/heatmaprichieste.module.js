(function ()
{
    'use strict';

    angular
        .module('app.heatmap', ['ngTable'])
        .config(config);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider,msNavigationServiceProvider)
    {


      // State
      $stateProvider
      .state('app.heatmap', {
          url    : '/heatmap',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/heatmaprichieste/heatmap.html',
                  controller : 'HeatmapController as vm'
              }
          }

      });



      msNavigationServiceProvider.saveItem('users.heatmap', {
          title    : 'Heatmap',
          icon     : 'icon-map-marker-radius',
          state    : 'app.heatmap',

          weight   : 3
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
