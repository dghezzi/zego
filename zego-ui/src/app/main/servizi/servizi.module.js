(function ()
{
    'use strict';

    angular
        .module('app.servizi', [

            'app.servizi.list',
            'ngTable'

        ])
        .config(config);

    /** @ngInject */
    function config(msNavigationServiceProvider,$stateProvider)
    {

        // Navigation
      /*  msNavigationServiceProvider.saveItem('pages', {
            title : 'PAGES',
            group : true,
            weight: 2
        });
        */
        $stateProvider
        .state('app.servizi', {
            url    : '/servizi',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/servizi/list.html',
                    controller : 'ServiziListController as vm'
                }
            }
        });



        msNavigationServiceProvider.saveItem('zoneservizi.servizi', {
            title : 'Servizi',
            state : 'app.servizi',
            icon :'icon-city',
            weight: 2
        });

    }
})();
