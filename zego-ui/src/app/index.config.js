(function ()
{
    'use strict';

    angular
        .module('fuse')
        .config(config)
        .filter('secondsToDateTime', [function() {
            return function(seconds) {
                return new Date(1970, 0, 1).setSeconds(seconds);
            };
        }]);

    /** @ngInject */
    function config($translateProvider,uiGmapGoogleMapApiProvider,$mdDateLocaleProvider,ngIntlTelInputProvider)
    {
      $mdDateLocaleProvider.formatDate = function(date) {
       return moment(date).format('DD/MM/YYYY');
     };

     ngIntlTelInputProvider.set({initialCountry: 'it',
      preventInvalidNumbers:true,
      autoFormat:false

   });



        // Put your common app configurations here
    uiGmapGoogleMapApiProvider.configure({
           key: 'AIzaSyBqzrTWq8JKZTiJ2OasE4-DbQW8phviCbU',
            v: '3.25', //defaults to latest 3.X anyhow
            libraries: 'weather,geometry,visualization'
        });

        // angular-translate configuration
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: '{part}/i18n/{lang}.json'
        });
        $translateProvider.preferredLanguage('en');
        $translateProvider.useSanitizeValueStrategy('sanitize');
    }





    angular.module('fuse')
    .directive('fastExport', function($filter) {

        return {
          restrict: 'A',
          scope: {
            fastExport: '@'
          },
          link: function(scope, elem, attrs) {

              elem.bind('click', function() {
                var lista;
                var nomeFile = "export";

                if(typeof attrs.fastExport == 'string' ){
                    lista = JSON.parse(attrs.fastExport);
                    if(attrs.exportName)
                    nomeFile = attrs.exportName;
                }

                var colonne = [];
                angular.forEach(lista,function(o){
                  Object.keys(o).forEach(function(k){
                    if( colonne.indexOf(k)==-1 ){
                      colonne.push(k)
                    }
                  })
                })

                var oggi = new Date();
                nomeFile += "-"+oggi.getDate() + "-" + (oggi.getMonth()+1)+"-"+oggi.getFullYear();

                var csvHead = "data:text/csv;charset=utf-8,";
                var csvContent = "";

                // Prima colonna, intestazione
                var a = [];
                colonne.forEach(function(c){

                    a.push( c )

                })
                csvContent += ( a.join(";") ) + '\n';
                //-->

                // Aggiungo elementi
                angular.forEach(lista,function(o){
                  var a = [];

                  colonne.forEach(function(c){
                    var d = new Date( o[c] * 1000 )
                    if(o[c] && o[c].length == 10 &&  typeof d.getMonth === 'function' ){

                      a.push( $filter("date")(o[c] *1000, "medium"))
                    }else{
                      a.push( o[c] == undefined ? '' : o[c] )
                    }
                  })

                  a = a.join(";");
                  csvContent += a + '\n';
                });
                //-->


                var link = document.createElement('a');
                link.download = nomeFile+".csv";
                link.href = csvHead + encodeURIComponent(csvContent);
                link.click();

                //window.open(encodeURI(csvContent));

              });
          }
        };

      })


})();

var DEFAULT_POPOVER_URL = 'angular-bootstrap-confirm.html';
