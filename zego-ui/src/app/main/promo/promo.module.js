(function ()
{
    'use strict';

    angular
        .module('app.promo', [

            'app.promo.list',
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
        .state('app.promo', {
            url    : '/promo',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/promo/list.html',
                    controller : 'PromoListController as vm'
                }
            }
        }).state('app.promonew', {
            url    : '/nuova-promo/:id',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/promo/edit.html',
                    controller : 'PromoNewController as vm'
                }
            }
        }).state('app.promoedit', {
            url    : '/edit-promo/:id',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/promo/edit.html',
                    controller : 'PromoEditController as vm'
                }
            }
        });


        msNavigationServiceProvider.saveItem('promo', {
            title : 'Promo',
            state : 'app.promo',
            icon :'icon-map',
            weight: 10
        });

    }
})();
