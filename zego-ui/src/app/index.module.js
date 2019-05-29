(function ()
{
    'use strict';


    /**
     * Main module of the Fuse
     */
    angular
        .module('fuse', [
            'ngTable',
             'uiGmapgoogle-maps',
             'mdPickers',
             'ngIntlTelInput',
             'angular-md5',
            // Core
            'app.core',

            // Navigation
            'app.navigation',

            // Toolbar
            'app.toolbar',

            // Quick Panel
            'app.quick-panel',

            // Pages
            'app.pages',
            'app.dialog',
            'app.zone',
            'app.servizi',
            'app.riderequest',
            'app.heatmap',
            'app.aree',
            'app.promo',
            'app.driver',
            'app.mappadriver',
            'app.report',

            //User
            'app.user',

            //login
            'app.auth.login',

            //Conf
            'app.impostazioni',

            'app.version',
            'app.backenduser',
            'app.blacklistdevice',
            'mm.iban',
            'ngCsvImport'

        ]);
})();
