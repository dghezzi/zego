(function ()
{
    'use strict';

    angular
        .module('app.driver.list',['ngTable'])
        .config(config);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider,msNavigationServiceProvider)
    {
      // State
      $stateProvider
      .state('app.driverlist', {
          url    : '/driver',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/driver/list/list.html',
                  controller : 'DriverListController as vm'
              }
          }

      });




      msNavigationServiceProvider.saveItem('users.driver', {
          title    : 'Drivers',
          icon     : 'icon-account-multiple',
          state    : 'app.driverlist',
          weight   : 2
      });

    }



    /*

    // Navigation
    msNavigationServiceProvider.saveItem('users', {
        title : 'USERS',
        group : true,
        weight: 1
    });

    msNavigationServiceProvider.saveItem('users.list', {
        title    : 'List',
        icon     : 'icon-tile-four',
        state    : 'app.sample',

        translate: 'SAMPLE.SAMPLE_NAV',
        weight   : 1
    });
    */

})();
