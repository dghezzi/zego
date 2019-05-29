(function ()
{
    'use strict';

    angular
        .module('app.blacklistdevice', [

            'app.blacklistdevice.list',
            'ngTable'

        ])
        .config(config);

    /** @ngInject */
    function config(msNavigationServiceProvider,$stateProvider)
    {

      $stateProvider
      .state('app.blacklist', {
          url    : '/blacklist',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/blacklistdevice/list.html',
                  controller : 'BlacklistdeviceListController as vm'
              }
          }

      });
      msNavigationServiceProvider.saveItem('blacklistdevicelist', {
          title    : 'Device blacklist',
          icon     : 'icon-account-multiple',
          state    : 'app.blacklist',


          weight   : 1
      });


    }
})();
