(function ()
{
    'use strict';

    angular
        .module('app.backenduser')
        .controller('BackenduserViaggiController', BackenduserViaggiController)
        .controller('CropImg', CropImg)
        .controller('BlankDialog', BlankDialog)
        .controller('BackenduserProfiloController', BackenduserProfiloController)
        .directive("cf", function () {
  return {
    restrict: "A",
    require: "ngModel",
    link: function(scope, element, attr, mCtrl) {

      function controlloCF(cf)
      {
        var okchar, i, s, set1, set2, setpari, setdisp;

        if(!cf || cf == '') {
          mCtrl.$setValidity('cf',true);
          mCtrl.$setPristine(true);
          return cf;
        }
        if(cf.length != 16 ) {
          mCtrl.$setValidity('cf', false);
          return cf;
        }

        cf = cf.toUpperCase();

        okchar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for( i = 0; i < 16; i++ ){
          if( okchar.indexOf( cf.charAt(i) ) == -1 ) {
            mCtrl.$setValidity('cf', false);
            return cf;
          }
        }

        set1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        set2 = "ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ";
        setpari = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        setdisp = "BAKPLCQDREVOSFTGUHMINJWZYX";
        s = 0;
        for( i = 1; i <= 13; i += 2 )
          s += setpari.indexOf( set2.charAt( set1.indexOf( cf.charAt(i) )));
        for( i = 0; i <= 14; i += 2 )
          s += setdisp.indexOf( set2.charAt( set1.indexOf( cf.charAt(i) )));
        if( s%26 != cf.charCodeAt(15)-'A'.charCodeAt(0))
        {
          mCtrl.$setValidity('cf', false);
          return cf;
        }
        else
        {
          mCtrl.$setValidity('cf', true);
          return cf;
        }

        return cf;
      }
      mCtrl.$parsers.push(controlloCF);
    }
  };
});
       ;
    /** @ngInject */

    function BlankDialog(){
    }
    function BackenduserViaggiController(api,$stateParams,$scope,$mdDialog,$state,NgTableParams,$rootScope){
      var id = $rootScope.userLogged.id;
      var start = 0;
      var stop = 100000;

      $scope.settimanaCorrente = getDays( moment());

      $scope.settimanaPrecedente = function(){
          var lunediMom = moment($scope.settimanaCorrente.lunedi);
          //var domenicaP = $scope.settimanaCorrente.lunedi.getTime() - (3600 * 24 * 1000);
          var domenicaP = lunediMom.subtract(1, 'day');
          $scope.settimanaCorrente = getDays(domenicaP);
          caricaViaggi();
      }

      $scope.settimanaSuccessiva = function(){
         var domenicaMom = moment($scope.settimanaCorrente.domenica);
          //var lunediP = $scope.settimanaCorrente.domenica.getTime() + (3600 * 24 * 1000);
           var lunediP = domenicaMom.add(1, 'day');
          $scope.settimanaCorrente = getDays(lunediP);
          caricaViaggi();
      }
$scope.totalcard=0;
$scope.totalcontanti=0;
$scope.totalcardzego=0;
$scope.totalcontantidriver=0;

      $scope.card = function (item) {

    return item.method != 'cash';
};

caricaPagamentiContanti();

  function caricaPagamentiContanti(){
        api.user.single.pagamentiContanti({id : id,start:0,stop:100}).$promise.then(function(r){
            $scope.pagamenticontanti = r;
            $scope.tabellaPagamentiContanti = new NgTableParams({},{dataset : $scope.pagamenticontanti});

        });

      }

      function getDays(d){
        /*d = new Date(d);
        var day = d.getDay(),
            diff = d.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
        var lunedi = new Date(d.setDate(diff));
        var lt = lunedi.getTime();
        var st = lt + (3600 * 24 * 1000 * 6);

        var domenica = new Date(st);*/
        var day =  d.day();

        if(day==0){
          var lunedi=d.clone().subtract(6,'day').toDate();
          var domenica=d.toDate();
        }
        else{
          var lunediM=d.subtract(day-1,'day');
          var lunedi = lunediM.toDate();
          var domenica=lunediM.clone().add(6,'day').toDate();
        }

        return {
          lunedi : lunedi,
          domenica : domenica
        }
      }

      function caricaViaggi(){

        var ricerca = new api.oggettoRicerca();

        var l = $scope.settimanaCorrente.lunedi;
        l.setHours(0,0,0,0);
        var d = $scope.settimanaCorrente.domenica;
        d.setHours(23,59,0,0);
        ricerca.addRange("reqdate",Math.round(l.getTime() / 1000),Math.round(d.getTime() / 1000));
        ricerca.orderBy("id","DESC");
        ricerca.addField("status",9);


        api.storicoviaggi.comepasseggero({
          start : Math.round(l.getTime() / 1000),
          stop:Math.round(d.getTime() / 1000),
          uid : id
        }).$promise.then(function(data){
          $scope.viaggiPasseggero = data;


          $scope.tabellaViaggiPasseggero = new NgTableParams({},{dataset : $scope.viaggiPasseggero});

          var totale=0;
          angular.forEach($scope.viaggiPasseggero,function(o){
            if(o.passprice != undefined){
              totale += o.passprice;
            }
          })

          $scope.totaleDaPasseggero = totale;


        });

        api.storicoviaggi.comedriver({
          start : Math.round(l.getTime() / 1000),
          stop:Math.round(d.getTime() / 1000),
          uid : id
        }).$promise.then(function(data){
          $scope.viaggiDriver = data;

          $scope.tabellaViaggiDriver = new NgTableParams({},{dataset : $scope.viaggiDriver});


          var totale=0;
          angular.forEach($scope.viaggiDriver,function(o){
            if(o.driverfee != undefined){
              totale += o.driverfee;
            }
          })

          $scope.totaleDaDriver = totale;


        });
        /*
        var ricercaDriver = angular.copy(ricerca);
        ricercaDriver.addField("did",$rootScope.userLogged.id);



         api.advancedSearch.post({
           entity :'riderequest',
           start  :start,
           stop : stop
         },ricercaDriver).$promise.then(function(data){

            $scope.viaggiDriver = data;
            $scope.tabellaViaggiDriver = new NgTableParams({},{dataset : $scope.viaggiDriver});
            var totale=0;
            angular.forEach($scope.viaggiDriver,function(o){
              if(o.driverprice != undefined){
                totale += o.driverprice;
              }
            })

            $scope.totaleDaDriver = totale;
         });



         var ricercaPass = angular.copy(ricerca);
         ricercaPass.addField("pid",$rootScope.userLogged.id);
         api.advancedSearch.post({
            entity :'riderequest',
            start  :start,
            stop : stop
          },ricercaPass).$promise.then(function(data){

             $scope.viaggiPasseggero = data;
             $scope.tabellaViaggiPasseggero = new NgTableParams({},{dataset : $scope.viaggiPasseggero});

             var totale=0;
             angular.forEach($scope.viaggiPasseggero,function(o){
               if(o.passprice != undefined){
                 totale += o.passprice;
               }
             })

             $scope.totaleDaPasseggero = totale;
          });
          */
      }


      caricaViaggi();
      /*$scope.caricaViaggiDriver();
      $scope.caricaViaggiPasseggero();*/

    }
    function CropImg(api,$stateParams,$scope,$mdDialog,$state,NgTableParams,$rootScope,file){
      if( HTMLCanvasElement.toBlob == undefined ){
                                           HTMLCanvasElement.prototype.toBlob = function(c){
//                                               debugger;
                                                var dataURL = this.toDataURL();
                                                var blobBin = atob(dataURL.split(',')[1]);
                                                var array = [];
                                                var i;

                                                 //<![CDATA[
                                                for(i = 0; i < blobBin.length ; i++) {
                                                    array.push(blobBin.charCodeAt(i));
                                                }
                                                var file=new Blob([(new Uint8Array(array)).buffer], {type: 'image/png'});

                                                c(file);
                                                //]]>
                                           }
                                       }


      var filetype;
         var cropper;
         var originalFilename;
         var origfiletype;

         $scope.options = {
            maximize: true,
            aspectRatio: 2 / 1,
            crop: function(dataNew) {
              data = dataNew;
            }
          };

         var reader  = new FileReader();

         var img = angular.element( document.querySelector( '#immagineDaCroppare' ) )
        originalFilename = file.name;
        origfiletype = file.type;

        reader.addEventListener("load", function () {

              //$(img).attr("src",reader.result);
              $scope.imgsrc=reader.result;



              window.setTimeout(function(){
                var image = document.getElementById('immagineDaCroppare');


                cropper = new Cropper(image, {
                  aspectRatio: 1 / 1,
                  crop: function(e) {
                    console.log(e.detail.x);
                    console.log(e.detail.y);
                    console.log(e.detail.width);
                    console.log(e.detail.height);
                    console.log(e.detail.rotate);
                    console.log(e.detail.scaleX);
                    console.log(e.detail.scaleY);
                  }
                });
              },500)



          }, false);

          if (file) {
            reader.readAsDataURL(file);
          }


          $scope.salva = function(){
            console.log(cropper);
            var c = cropper.getCroppedCanvas();

            c.toBlob(function (blob) {
                file = blob;

                 $scope.answer(file);

            });

          }



          $scope.answer = function(answer) {
            $mdDialog.hide(answer);
          };
          $scope.cancel = function() {
              $mdDialog.cancel();
          };




    }
    function BackenduserProfiloController(api,$stateParams,$scope,$mdDialog,$state,NgTableParams,$rootScope)
    {

      function getManufacturer(){
        api.manufacturer.getAll().$promise.then(function(o){
           $scope.listaManufacturer = o;

           console.log(o);

        })
      }

      getManufacturer();


      function codiceFISCALE(cfins,data)
       {
             if( !data ) return false;

             if( !(typeof data.getMonth == 'function') ){
               data = new Date(data * 1000);
             }

             var anno = data.getFullYear().toString().substr(2,2);
             var mese = (data.getMonth()+1).toString();
             var giorno = data.getDate();


             if(cfins.substr(6,2) != anno){
               return false;
             }

             var cf = cfins.toUpperCase();
             var cfReg = /^[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]$/;
             if (!cfReg.test(cf))
                return false;
             var set1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
             var set2 = "ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ";
             var setpari = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
             var setdisp = "BAKPLCQDREVOSFTGUHMINJWZYX";
             var s = 0;
             var i;
             for( i = 1; i <= 13; i += 2 )
                s += setpari.indexOf( set2.charAt( set1.indexOf( cf.charAt(i) )));
             for( i = 0; i <= 14; i += 2 )
                s += setdisp.indexOf( set2.charAt( set1.indexOf( cf.charAt(i) )));
             if ( s%26 != cf.charCodeAt(15)-'A'.charCodeAt(0) )
                return false;

             return true;
       }

      function mod97(string) {
          var checksum = string.slice(0, 2), fragment;
          for (var offset = 2; offset < string.length; offset += 7) {
              fragment = String(checksum) + string.substring(offset, offset + 7);
              checksum = parseInt(fragment, 10) % 97;
          }
          return checksum;
      }


      function isValidIBANNumber(input) {
          var CODE_LENGTHS = {
              AD: 24, AE: 23, AT: 20, AZ: 28, BA: 20, BE: 16, BG: 22, BH: 22, BR: 29,
              CH: 21, CR: 21, CY: 28, CZ: 24, DE: 22, DK: 18, DO: 28, EE: 20, ES: 24,
              FI: 18, FO: 18, FR: 27, GB: 22, GI: 23, GL: 18, GR: 27, GT: 28, HR: 21,
              HU: 28, IE: 22, IL: 23, IS: 26, IT: 27, JO: 30, KW: 30, KZ: 20, LB: 28,
              LI: 21, LT: 20, LU: 20, LV: 21, MC: 27, MD: 24, ME: 22, MK: 19, MR: 27,
              MT: 31, MU: 30, NL: 18, NO: 15, PK: 24, PL: 28, PS: 29, PT: 25, QA: 29,
              RO: 24, RS: 22, SA: 24, SE: 24, SI: 19, SK: 24, SM: 27, TN: 24, TR: 26
          };
          var iban = String(input).toUpperCase().replace(/[^A-Z0-9]/g, ''), // keep only alphanumeric characters
                  code = iban.match(/^([A-Z]{2})(\d{2})([A-Z\d]+)$/), // match and capture (1) the country code, (2) the check digits, and (3) the rest
                  digits;
          // check syntax and length
          if (!code || iban.length !== CODE_LENGTHS[code[1]]) {
              return false;
          }
          // rearrange country code and check digits, and convert chars to ints
          digits = (code[3] + code[1] + code[2]).replace(/[A-Z]/g, function (letter) {
              return letter.charCodeAt(0) - 55;
          });
          // final check
          return mod97(digits);
      }

      $scope.stati = api.langIsoAll();
      $scope.requestStatusLabel = api.requestStatusLabel;

      $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };


      $scope.nuovaCarImg = function(){
        $scope.driverdata.carimg='';

      }

      /*$scope.$watch("driverdata.iban",function(){
        if(!$scope.driverdata)return;


        if(isValidIBANNumber($scope.driverdata.iban)){
          $scope.ibanerror = "";
        }else {
          $scope.ibanerror = "IBAN non valido"
        }
      })*/

      /*$scope.$watch("driverdata.cf",function(){
         $scope.cferrore = undefined;
        if(!$scope.driverdata) return;
        if(!$scope.driverdata.cf)return;
                console.log("driverdata.cf",$scope.driverdata.cf);

        if($scope.driverdata.status == 2 || $scope.driverdata.status == 5) return;

        if(codiceFISCALE($scope.driverdata.cf,vm.user.birthdate)){
          $scope.cferrore = undefined;
        }else {
          $scope.cferrore = "Codice fiscale non valido"

        }
      })*/

      $scope.vediFoto = function(ev) {
        $scope.fotoDialog = $mdDialog.show({

          contentElement: '#myDialog',
          parent: angular.element(document.body),
          targetEvent: ev,
          clickOutsideToClose: true
        });
      };


      $scope.getStati = function(){
        api.nation.query().$promise.then(function(d){
            $scope.stati = d;
        })

      }

      $scope.getStati();

      $scope.getCities = function(t){
          return api.cities.query({pfx:t}).$promise;
      }
      $scope.getCitiesRes = function(t){
          return api.cities.query({pfx:t}).$promise;
      }


      $scope.blacklisteddevices = [];





      var vm = this;
      $scope.navigazione = {
          mode : "edit"
      };


      $scope.effettuaRichiesta = function(){

        if(
          (!$scope.driverdata.carimg || $scope.driverdata.carimg=='') ||
          (!$scope.driverdata.brand || $scope.driverdata.brand=='')||
          (!$scope.driverdata.model || $scope.driverdata.model=='')||
          (!$scope.driverdata.color || $scope.driverdata.color=='')||
          (!$scope.driverdata.plate || $scope.driverdata.plate=='')||
          (!$scope.driverdata.seat || $scope.driverdata.seat=='')||
          (!$scope.driverdata.year || $scope.driverdata.year=='')
        ){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Errore')
              .textContent('Dati veicolo mancanti.')
              .ok('Ok')
          );
          return;
        }

        if(
          (!vm.user.fname || vm.user.fname=='') ||
          (!vm.user.lname || vm.user.lname=='')||
          (!vm.user.birthdate || vm.user.birthdate=='')||
          (!$scope.driverdata.birthcity || $scope.driverdata.birthcity=='')||
          (!$scope.driverdata.birthcountry || $scope.driverdata.birthcountry=='')||
          (!$scope.driverdata.cf || $scope.driverdata.cf=='')||
          (!$scope.driverdata.address|| $scope.driverdata.address=='')||
          (!$scope.driverdata.iban|| $scope.driverdata.iban=='')||
          (!$scope.driverdata.area || $scope.driverdata.area=='')
        ){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Errore')
              .textContent('Dati utente mancanti.')
              .ok('Ok')
          );
          return;
        }



        if(
          (!$scope.driverdata.docexpdate || $scope.driverdata.docexpdate=='')||
          (!$scope.driverdata.insuranceimg || $scope.driverdata.insuranceimg=='')||
          (!$scope.driverdata.docimg || $scope.driverdata.docimg=='')

        ){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Errore')
              .textContent('Controlla e carica i documenti richiesti.')
              .ok('Ok')
          );
          return;
        }

        if( !isValidIBANNumber($scope.driverdata.iban) ){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Errore')
              .textContent('Controlla che l\'iban inserito sia valido.')
              .ok('Ok')
          );
          return;
        }


        if( !codiceFISCALE($scope.driverdata.cf,vm.user.birthdate) ){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title('Errore')
              .textContent('Controlla che il codice fiscale sia valido.')
              .ok('Ok')
          );
          return;
        }

        $scope.driverdata.status = 5; // documenti inviati
        $scope.salvaModifica();
      }



            function convertDate(inputFormat) {
              function pad(s) { return (s < 10) ? '0' + s : s; }
              var d = new Date(inputFormat);
              return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('/');
            }



      $scope.salvaModifica = function(){

        var user = angular.copy(vm.user);



        if( user.birthdate!= undefined ){
          if( typeof user.birthdate.getMonth == 'function'){
              user.birthdate = convertDate(user.birthdate.getTime());
          }
        }

        for (var property in user) {
          if (user.hasOwnProperty(property)) {
              if(typeof user[property].getMonth === 'function'){
                  user[property] = Math.round(user[property].getTime() / 1000);
              }
          }
        }


        api.user.single.put({id : user.id},user,function(r){

            salvaDriverData(function(o){
              //$scope.navigazione.mode="info";
              $mdDialog.show(
                $mdDialog.alert()
                  .clickOutsideToClose(true)
                  .parent(angular.element(document.body))
                  .title('Utente salvato')
                  .textContent('Utente salvato correttamente.')
                  .ok('Go')
              );
            })
         })
      }

      function salvaDriverData(c){

        console.log($scope.driverdata);
        if(!$scope.driverdata || !$scope.driverdata.id)
        return;


        if( $scope.driverdata.birthcountry != undefined ){
          console.log($scope.driverdata.birthcountry);
        }

        var driverdata = angular.copy($scope.driverdata);


        if(driverdata.birthcity && driverdata.birthcity.name){
          driverdata.birthcity = driverdata.birthcity.name
        }

        if(driverdata.city && driverdata.city.name){
          driverdata.city = driverdata.city.name
        }

        for (var property in driverdata) {
          if (driverdata.hasOwnProperty(property)) {
              if(driverdata[property] && typeof driverdata[property].getMonth === 'function'){
                  driverdata[property] = Math.round(driverdata[property].getTime() / 1000);
              }
          }
        }
        api.driverdata.save({id:driverdata.id},driverdata).$promise.then(function(o){

          if(c!=undefined){
            c(o);
          }
          //$scope.navigazione.mode="info";

        })

      }

      var filetype;
     var cropper;
     var originalFilename;
     var origfiletype;
      vm.referente = null;

      $scope.carImgCambiato = function(t){
        if(t.files.length == 0) return;



        var file = t.files[0];

        if(file.size > 6097152){
          alert("La dimensione massima è 5 mega");

          return;
        }

        originalFilename = file.name;
        origfiletype = file.type;
        $mdDialog.show({
           controller: CropImg,
           templateUrl: 'app/backenduser/crop.html',
           parent: angular.element(document.body),

           locals : {
                file : t.files[0]

           },
           clickOutsideToClose:true,
           fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
         })
       .then(function(answer) {

         var dialog = $mdDialog.show({
            controller: BlankDialog,
            templateUrl: 'app/backenduser/popup-caricamento.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true,
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
          })
        .then(function(answer) {

        }, function() {
          $scope.status = 'You cancelled the dialog.';
        });



             api.aws.upload(answer,originalFilename,origfiletype).then(function(o){
                 $mdDialog.hide();
               $scope.driverdata.carimg = o.Location;
               salvaDriverData();
             })

       }, function() {

       });


        /*

        var file = t.files[0];
        api.aws.upload(file).then(function(o){

          $scope.driverdata.carimg = o.Location;


          salvaDriverData();
        })
        */
      }

      $scope.assicurazioneCambiato = function(t){

        var file = t.files[0];

        if(file.size > 6097152){
          alert("La dimensione massima è 5 mega");

          return;
        }

        var dialog = $mdDialog.show({
           controller: BlankDialog,
           templateUrl: 'app/backenduser/popup-caricamento.html',
           parent: angular.element(document.body),
           clickOutsideToClose:true,
           fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
         })
       .then(function(answer) {

       }, function() {
         $scope.status = 'You cancelled the dialog.';
       });


        api.aws.upload(file).then(function(o){
          console.log(o);

          $scope.driverdata.insuranceimg = o.Location;

          $mdDialog.hide();
          salvaDriverData();
          $scope.messaggioAggiornaPatente = "Hai caricato un nuovo documento, aggiorna la data di scadenza!";
        })
      }
      $scope.patenteCambiato = function(t){

        var file = t.files[0];
        if(file.size > 6097152){
          alert("La dimensione massima è 5 mega");

          return;
        }

        var dialog = $mdDialog.show({
           controller: BlankDialog,
           templateUrl: 'app/backenduser/popup-caricamento.html',
           parent: angular.element(document.body),
           clickOutsideToClose:true,
           fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
         })
       .then(function(answer) {

       }, function() {
         $scope.status = 'You cancelled the dialog.';
       });


        api.aws.upload(file).then(function(o){
          console.log(o);
          $scope.driverdata.docimg = o.Location;

          $mdDialog.hide();
          salvaDriverData();
          $scope.messaggioAggiornaPatente = "Hai caricato un nuovo documento, aggiorna la data di scadenza!";

        })
      }


      api.user.single.get({id : $rootScope.userLogged.id},
        function(result){
          vm.user = result;

          if(vm.user.birthdate){
            try {
              var dateString = vm.user.birthdate; // Oct 23
              var dateParts = dateString.split("/");
              vm.user.birthdate = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
            } catch (e) {

              vm.user.birthdate = new Date(vm.user.birthdate * 1000);
            } finally {

            }
          }
            //vm.user.birthdate = new Date(vm.user.birthdate);

          api.user.single.getDriverData({id:vm.user.id}).$promise.then(function(r){


            if(r.length>0){
              if(r[0].docexpdate != undefined){
                r[0].docexpdate = new Date(r[0].docexpdate * 1000);
              }
              $scope.driverdata = r[0];

              if($scope.driverdata.expdate)
              $scope.driverdata.expdate = new Date(vm.user.expdate);

            }else{
              $scope.driverdata = {};
            }


                console.log($scope.driverdata);
          })

          if(vm.user.refuid){
              api.user.single.get({id:vm.user.refuid},function(r){
                vm.referente = r;
              })
          }
          console.log(result);

        },
        function(error){
          console.log(error);
        }
      );

    }







})();
