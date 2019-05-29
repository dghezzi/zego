(function ()
{
    'use strict';

    angular
        .module('app.riderequest.list',['ngTable'])
        .config(config);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider,msNavigationServiceProvider)
    {
      // State
      $stateProvider
      .state('app.riderequestlist', {
          url    : '/riderequest/list',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/riderequest/list/list.html',
                  controller : 'RiderequestListController as vm'
              }
          }

      }).state('app.riderequestedit', {
          url    : '/riderequest/edit/:id',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/riderequest/list/edit.html',
                  controller : 'EditRiderequestController as vm'
              }
          }
      });



      msNavigationServiceProvider.saveItem('users.riderequest', {
          title    : 'Lista Ride Request',
          icon     : 'icon-car',
          state    : 'app.riderequestlist',

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
