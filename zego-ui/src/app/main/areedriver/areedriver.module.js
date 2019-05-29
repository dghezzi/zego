(function ()
{
    'use strict';

    angular
        .module('app.aree', [

            'app.aree.list',
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
        .state('app.aree', {
            url    : '/aree',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/areedriver/list.html',
                    controller : 'AreeListController as vm'
                }
            }
        });



        msNavigationServiceProvider.saveItem('zoneservizi.aree', {
            title : 'Aree richieste driver',
            state : 'app.aree',
            icon :'icon-city',
            weight: 2
        });

    }
})();
