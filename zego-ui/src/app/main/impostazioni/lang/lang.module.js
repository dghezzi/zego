(function ()
{

    'use strict';



    angular
        .module('app.lang',['app.lang.list'])
        .config(config);

    /** @ngInject */
    function config(msNavigationServiceProvider,  $stateProvider)
    {


      $stateProvider
      .state('app.lang', {
          url    : '/lang',
          views  : {
              'content@app': {
                  templateUrl: 'app/main/impostazioni/lang/list.html',
                  controller : 'LangListController as vm'
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



      msNavigationServiceProvider.saveItem('impostazioni.messaggi.lang', {
          title : 'Modifica lingue',
          state : 'app.lang',
          icon : 'icon-flag-variant',
          weight: 2
      });

    }
})();
