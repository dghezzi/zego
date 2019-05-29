(function ()
{

    'use strict';



    angular
        .module('app.conf',['app.conf.list'])
        .config(config);

    /** @ngInject */
    function config(msNavigationServiceProvider,  $stateProvider)
    {

      $stateProvider
      .state('app.conf', {
          url    : '/conf',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/impostazioni/conf/list.html',
                  controller : 'ConfListController as vm'
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

        // Navigation


        msNavigationServiceProvider.saveItem('impostazioni.app.conf', {
            title : 'Modifica configurazioni',
            state : 'app.conf',
            icon :'icon-format-list-bulleted',
            weight: 2
        });

    }
})();
