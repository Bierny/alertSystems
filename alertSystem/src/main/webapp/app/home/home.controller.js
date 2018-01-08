(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope','TestDab', 'Principal', 'LoginService', '$state','$interval', '$rootScope','$uibModal'];

    function HomeController ($scope, TestDab,Principal, LoginService, $state,$interval,$rootScope,$uibModal) {
        var vm = this;
        $rootScope.fromHome = true;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });
        $scope.loading = false;
        loadAll();
        getAccount();
        function loadAll () {
            $scope.loading=true;
            TestDab.query({},onSuccess, onError);

            function onSuccess(data, headers) {
               $scope.alerts = data;
                var data2 = data.filter($scope.myFilter);
               $scope.id= $rootScope.id = data2[0].id;
                $scope.loading=false;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        var timerData =
            $interval(function () {
                if(!$scope.loading){
                    loadAll();
                }

            }, 50000);

        $scope.myFilter = function (item) {
            return item.incidentStatus =='SENT';
        };

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function reloadData(){

        }
        $scope.openMap = function(location){
            $scope.loc = location;
            $uibModal.open({
                templateUrl: 'app/entities/incident/incident-map-dialog.html',
                controllerAs: 'vm',
                size: 'md',
                controller: 'IncidentNextController',

                resolve: {
                    entity: ['Incident', function(Incident) {
                        return Incident.get({id : 1}).$promise;
                    }]
                }
            }).result.then(function() {
                    $state.go('incident', null, { reload: 'false' });
                }, function() {
                    $state.go('^');
                });
        }


        $scope.alerts = [{
            notifier:{
            name:"Sam",
                surname:"Johnes",
                phone: "509546546"
        },
        location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
    },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        },{
            notifier:{
                name:"Sam",
                surname:"Johnes",
                phone: "509546546"
            },
            location:"54165165x561651",
            inDanger:"true",
            kind: "No bo tak to lecialeo ze to nie mozne jest",
            otherCircumstances: "No to wszsytko nylo dziwne i nie mozliwe"
        }];
        function register () {
            $state.go('register');
        }
        $(window).on("load resize ", function() {
            var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
            $('.tbl-header').css({'padding-right':scrollWidth});
        }).resize();
    }
})();
