(function ()
{
    'use strict';

    angular
        .module('app.auth.login', [])
        .config(config);

    /** @ngInject */
    function config($stateProvider, $translatePartialLoaderProvider, msNavigationServiceProvider)
    {


        // State
        $stateProvider.state('app.login', {
            url      : '/login',
            views    : {
                'main@'                       : {
                    templateUrl: 'app/core/layouts/content-only.html',
                    controller : 'MainController as vm'
                },
                'content@app.login': {
                    templateUrl: 'app/main/login/login.html',
                    controller : 'LoginController as vm'
                }
            },
            bodyClass: 'login'
        }).state('app.adminlogin', {
            url      : '/adminlogin',
            views    : {
                'main@'                       : {
                    templateUrl: 'app/core/layouts/content-only.html',
                    controller : 'MainController as vm'
                },
                'content@app.adminlogin': {
                    templateUrl: 'app/main/login/adminlogin.html',
                    controller : 'AdminLoginController as vm'
                }
            },
            bodyClass: 'login'
        })
        .state('app.logout', {
            url      : '/logout',
            views    : {
              'main@'                       : {
                  templateUrl: 'app/core/layouts/content-only.html',
                  controller : 'MainController as vm'
              },
                'content@app.logout': {
                    templateUrl: 'app/main/login/logout.html',
                    controller : 'LogoutController as vm'
                }
            },
            bodyClass: 'login'
        });
        // Translation
        $translatePartialLoaderProvider.addPart('app/main/login');
        $translatePartialLoaderProvider.addPart('app/main/user/list');

        /*msNavigationServiceProvider.saveItem('logout', {
            title    : 'Logout',
            icon     : 'logut',
            state    : 'app.logout',


            weight   : 1000
        });*/



    }

})();
