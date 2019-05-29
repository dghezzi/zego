(function ()
{
    'use strict';

    angular
        .module('app.auth.login')
        .controller('LoginController', LoginController)
        .controller('AdminLoginController', AdminLoginController)
        .controller('LogoutController', LogoutController);

    /** @ngInject */

    function LogoutController($scope,api,$state,$rootScope,$mdDialog){

      api.logout();
      $state.go("app.login");
    }
    function LoginController($scope,api,$state,$rootScope,$mdDialog,$location,$window)
    {


      function statusChangeCallback(response){
        console.log(response);
        if(response.status == "not_authorized"){
          alert("Non hai un profilo facebook collegato a Zego")
        }
        if(response.status == 'unknown'){
          FB.login(function(response){
              statusChangeCallback(response);
          })
        }
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
            console.log(response);

            api.callLogin({
              /*country : c.iso2 ,
              prefix : "+"+c.dialCode,
              mobile : n,*/
              fbid: response.authResponse.userID,
                os : "web",
                vos : "web",
                vapp : "web",

            /*  device : null,

              pushid : null,
              uid : null,

              deviceId : null,
              uxlang : null*/
            },function(o,obj){
              if(o && o.id){
                api.saveLoginPersistent(o);
                $rootScope.userLogged = o;
                $location.path("/");
              }else{

                messaggio(obj.data.title,obj.data.msg )
              }
            })

        }
      }
      $scope.facebookLogin = function(){


        FB.getLoginStatus(function(response) {


          statusChangeCallback(response);



        });
      }



        var vm = this;

        $scope.step=1;

        function messaggio(t,m){
          $mdDialog.show(
            $mdDialog.alert()
              .clickOutsideToClose(true)
              .parent(angular.element(document.body))
              .title(t)
              .textContent(m)
              .ok('OK')
          );

        }

        function resetForm(){
          vm.form.phone="";
          vm.form.pin ="";
          $scope.step=1;
        }
        $scope.avanti = function(){
          if( $scope.step==1){
            var c = $("#numeroTelefono").intlTelInput("getSelectedCountryData");
            var n = $("#numeroTelefono").intlTelInput("getNumber",intlTelInputUtils.numberFormat.NATIONAL);


            /*
            api.user.list.filter({
              field:'mobile',
              value : vm.form.phone
            }).$promise.then(function(o){
              if(o.length>0)
                vm.step=2;
                else {
                  messaggio("Non valido","Numero di telefono non valido")
                  resetForm();
                }
            })*/

            api.callLogin({
              country : c.iso2 ,
              prefix : "+"+c.dialCode,
              mobile : n,
              fbid: null,
                os : "web",
                vos : "web",
                vapp : "web",

            /*  device : null,

              pushid : null,
              uid : null,

              deviceId : null,
              uxlang : null*/
            },function(o,obj){
              if(o && o.id){
                $scope.userselected = o;
                $scope.step = 2;
              }else{

                messaggio(obj.data.title,obj.data.msg )
              }
            })
            return;
          }

          if( $scope.step==2 ){

              if($scope.userselected){

                api.login({
                  uid:$scope.userselected.id,
                  pin: vm.form.pin,
                  os : "web",
                  vos : "web",
                  vapp : "web",
                },function(o){
                    if( o ){
                      $location.path("/");
                    }else{
                      messaggio("Errore","Non è stato possibile accedere. Probabilmente il pin inserito è scaduto, oppure non è associato al numero di telefono scelto.")
                    }
                  })

              }

          }
        }

        $scope.resendPin = function(){
          $rootScope.loadingProgress = true;


            if($scope.userselected){
              api.user.single.resend({uid:$scope.userselected.id}).$promise.then(function(o){
                messaggio("Pin inviato","Controlla i tuoi sms. Ti abbiamo inviato un nuovo pin per accedere. Ricorda che potresti dover reinserire il pin anche sull'applicazione, quindi non lo perdere.")
              })
            }else{
              messaggio("Non valido","Numero di telefono non valido")
              resetForm();
            }

        }
        // Data

        // Methods

        //////////
    }







    function AdminLoginController($scope,api,$state,$rootScope,$mdDialog,$location,$window,md5)
    {
      var vm = this;
      $scope.login = function(){
          api.adminlogin({
              username : vm.form.email,
              password : md5.createHash(vm.form.password)
          },function(r){
              console.log(r);
              api.user.single.get({id:r.id}).$promise.then(function(r){
                api.saveLoginPersistent(r);
                $rootScope.userLogged = r;
                $location.path("/");

              })
          },function(){
            alert("we")
          })
      }
    }

})();
