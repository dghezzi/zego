(function ()
{
    'use strict';

    angular
        .module('app.zone', [

            'app.zone.list',
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
        .state('app.zone', {
            url    : '/zone',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/zone/list.html',
                    controller : 'ZoneListController as vm'
                }
            }
        }).state('app.zonenew', {
            url    : '/nuova-zona',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/zone/edit.html',
                    controller : 'ZoneNewController as vm'
                }
            }
        }).state('app.zoneedit', {
            url    : '/edit-zona/:id',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/zone/edit.html',
                    controller : 'ZoneEditController as vm'
                }
            }
        });

        // Navigation
        msNavigationServiceProvider.saveItem('zoneservizi', {
            title : 'Zone e Servizi',
           icon :'icon-map-marker-multiple',
            weight: 2
        });


        msNavigationServiceProvider.saveItem('zoneservizi.zone', {
            title : 'Zone',
            state : 'app.zone',
            icon :'icon-map',
            weight: 10
        });

    }
})();
