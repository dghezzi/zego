(function ()
{
    'use strict';

    angular
        .module('app.dialog', [

            'app.dialog.list',
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
        .state('app.dialog', {
            url    : '/dialog',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/dialog/list.html',
                    controller : 'DialogListController as vm'
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




        msNavigationServiceProvider.saveItem('impostazioni.messaggi.dialog', {
            title : 'Inapp dialogs',
            state : 'app.dialog',
            icon :'icon-alert-circle',
            weight: 10
        });

    }
})();
