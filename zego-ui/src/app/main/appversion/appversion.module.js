(function ()
{
    'use strict';

    angular
        .module('app.version', [

            'app.version.list',
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
        .state('app.appversion', {
            url    : '/appversion',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/appversion/list.html',
                    controller : 'AppversionListController as vm'
                }
            },
            resolve: {
                SampleData: function (msApi,api)
                {
                    /*var pagination = {
                      page : 0,
                      start : 0,
                      stop : 2,
                      num : 10
                    };
                    return   api.user.list.pag(pagination)
                    */
                    //return msApi.resolve('sample@get');

                }
            }
        });



        msNavigationServiceProvider.saveItem('impostazioni.app.appversion', {
            title : 'Versioni app',
            state : 'app.appversion',
            icon :'icon-cellphone-iphone',
            weight: 2
        });

    }
})();
