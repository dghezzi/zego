(function ()
{
    'use strict';

    angular
        .module('app.user.list',['ngTable'])
        .config(config);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider,msNavigationServiceProvider)
    {
      // State
      $stateProvider
      .state('app.userlist', {
          url    : '/user/list',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/user/list/list.html',
                  controller : 'UserListController as vm'
              }
          }

      }).state('app.useredit', {
          url    : '/user/edit/:id',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/user/list/edit.html',
                  controller : 'EditUserController as vm'
              }
          }
      }).state('app.usernew', {
          url    : '/user/new',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/user/list/new.html',
                  controller : 'NewUserController as vm'
              }
          }
      });



      msNavigationServiceProvider.saveItem('users', {
          title : 'SERVIZIO',
          group : true,
          weight: 1
      });

      msNavigationServiceProvider.saveItem('users.list', {
          title    : 'List',
          icon     : 'icon-account-multiple',
          state    : 'app.userlist',

          translate: 'USER.LISTA_UTENTI',
          weight   : 1
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
