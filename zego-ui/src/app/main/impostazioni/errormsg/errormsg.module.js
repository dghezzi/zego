(function ()
{

    'use strict';



    angular
        .module('app.errormsg',['app.errormsg.list'])
        .config(config);

    /** @ngInject */
    function config(msNavigationServiceProvider,  $stateProvider)
    {

      $stateProvider
      .state('app.errormsg', {
          url    : '/errormsg',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/impostazioni/errormsg/list.html',
                  controller : 'ErrorMsgListController as vm'
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


        msNavigationServiceProvider.saveItem('impostazioni.messaggi.errormsg', {
            title : 'Messaggi di errore',
            state : 'app.errormsg',
            icon : 'icon-message-alert',
            weight: 2
        });

    }
})();
