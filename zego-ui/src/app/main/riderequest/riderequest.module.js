(function ()
{
    'use strict';

    angular
        .module('app.riderequest', [

            'app.riderequest.list'

        ])
        .config(config);

    /** @ngInject */
    function config()
    {

       

      /*
      uiGmapGoogleMapApiProvider.configure({
          //    key: 'your api key',
          v: '3.25', //defaults to latest 3.X anyhow
          libraries: 'visualization'
      });

        // Navigation
      /*  msNavigationServiceProvider.saveItem('pages', {
            title : 'PAGES',
            group : true,
            weight: 2
        });
        */
    }
})();
