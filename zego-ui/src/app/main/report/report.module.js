(function ()
{
    'use strict';

    angular
        .module('app.report', [


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
        .state('app.report', {
            url    : '/report',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/report/list.html',
                    controller : 'ReportController as vm'
                }
            }
        }).state('app.reportmarketing', {
            url    : '/reportmarketing',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/report/list-marketing.html',
                    controller : 'ReportController as vm'
                }
            }
        }).state('app.reportoperativi', {
            url    : '/reportoperativi',
            views  : {
                'content@app': {
                    templateUrl: 'app/main/report/list-operativi.html',
                    controller : 'ReportController as vm'
                }
            }
        });


         msNavigationServiceProvider.saveItem('report', {
             title : 'Report',
            icon     : 'icon-chart-bar',
            weight: 2
        });


        msNavigationServiceProvider.saveItem('report.amministrativi', {
            title : 'Amministrativi',
            state : 'app.report',
            weight: 2
        });


        msNavigationServiceProvider.saveItem('report.marketing', {
            title : 'Marketing',
            state : 'app.reportmarketing',
            weight: 2
        });

          msNavigationServiceProvider.saveItem('report.operativi', {
            title : 'Operativi',
            state : 'app.reportoperativi',
            weight: 2
        });

    }
})();
