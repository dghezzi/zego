(function ()
{

    'use strict';



    angular
        .module('app.impostazioni',
          [
            'app.conf',
            'app.lang',
            'app.errormsg'
          ])
        .config(config);

    /** @ngInject */
    function config(msNavigationServiceProvider,  $stateProvider)
    {



        // Navigation
        msNavigationServiceProvider.saveItem('impostazioni', {
            title : 'Impostazioni',
            group : true,
            weight: 2
        });

        msNavigationServiceProvider.saveItem('impostazioni.app', {
            title : 'App',

            icon : 'icon-cellphone-iphone',
            weight: 2
        });
        msNavigationServiceProvider.saveItem('impostazioni.messaggi', {
            title : 'Messaggi',

            icon : 'icon-flag-variant',
            weight: 2
        });


    }
})();
