(function ()
{
    'use strict';

    angular
        .module('app.backenduser',['ngTable', 'utils'])

        .config(config)
        .run(run);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider,msNavigationServiceProvider)
    {
      // State
      $stateProvider
      .state('app.mydashboard', {
          url    : '/my/dashboard',
          views  : {
              'content@app': {
                  templateUrl: 'app/backenduser/profilo.html',
                  controller : 'BackenduserProfiloController as vm'
              }
          }
      }).state('app.myviaggi', {
          url    : '/my/viaggi',
          views  : {
              'content@app': {
                  templateUrl: 'app/backenduser/viaggi.html',
                  controller : 'BackenduserViaggiController as vm'
              }
          }
      });





    }


    function run(msNavigationService, AuthService){
      msNavigationService.saveItem('my', {
          title : 'My Zego',
          group : true,
          weight: 1,
           hidden: function ()
        {
            return AuthService.isAdmin();
        }
      });


      msNavigationService.saveItem('my.dashboard', {
          title    : 'Profilo',
          icon     : 'icon-account-multiple',
          state    : 'app.mydashboard',
          weight   : 1,
           hidden: function ()
        {
            return AuthService.isAdmin();
        }
      });

      msNavigationService.saveItem('my.viaggi', {
          title    : 'Viaggi',
          icon     : 'icon-map',
          state    : 'app.myviaggi',
          weight   : 1,
           hidden: function ()
        {
            return AuthService.isAdmin();
        }
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
