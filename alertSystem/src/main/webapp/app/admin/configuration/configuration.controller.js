(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('JhiConfigurationController', JhiConfigurationController);

    JhiConfigurationController.$inject = ['$filter','JhiConfigurationService','TestDab','$scope','$filter'];

    function JhiConfigurationController (filter,JhiConfigurationService,TestDab,$scope,$filter) {
        var vm = this;

        vm.allConfiguration = null;
        vm.configuration = null;
        TestDab.query({},onSuccess, onError);
        $scope.alerts =[];
        $scope.labels = ["Zagrożenie życia", "Brak zagrożenia"];

        $scope.labels2 = ["Przesłane", "W trakcie obsługi", 'Zakończone', 'Anulowane'];
        $scope.labels4 = ["1", "d 1 do 5", 'od 6 do 10', 'Więcej niż'];
        $scope.labels3 = ["Styczeń", 'Luty', 'Marzec', 'Kwiecień', 'Maj','Czerwiec', 'Lipiec','Sierpień', 'Wrzesień', 'Październik', 'Listopad','Grudzień'];
        $scope.options = {legend: {display: true}};
            $scope.data=[];
            $scope.data2=[];
            $scope.data3=[];
            $scope.data4=[];
            $scope.series=['Ilość zgłoszeń'];
            $scope.series4=['Ilość poszkodowanych'];
        function onSuccess(data, headers) {
            $scope.alerts = data;
            var inDanger = $filter('filter')($scope.alerts, expression);
            var inDangerNot = $filter('filter')($scope.alerts, expression2);
            var sent = $filter('filter')($scope.alerts, expression3);
            var progress = $filter('filter')($scope.alerts, expression4);
            var done = $filter('filter')($scope.alerts, expression5);
            var cancelled = $filter('filter')($scope.alerts, expression6);
            var uno = $filter('filter')($scope.alerts, expression11);
            var due = $filter('filter')($scope.alerts, expression12);
            var tre = $filter('filter')($scope.alerts, expression13);
            var quatro = $filter('filter')($scope.alerts, expression14);

            function exp1(item) {
                return item.fillingDate.search(/2018-01/) !=-1;

            }

            var one = $filter('filter')($scope.alerts, exp1);

            function exp2(item) {
                return item.fillingDate.search(/2018-02/) !=-1;

            }

            var two = $filter('filter')($scope.alerts, exp2);

            function exp3(item) {
                return item.fillingDate.search(/2018-03/) !=-1;

            }

            var three = $filter('filter')($scope.alerts, exp3);

            function exp4(item) {
                return item.fillingDate.search(/2018-04/) !=-1;

            }

            var four = $filter('filter')($scope.alerts, exp4);

            function exp5(item) {
                return item.fillingDate.search(/2018-05/) !=-1;

            }

            var five = $filter('filter')($scope.alerts, exp5);

            function exp6(item) {
                return item.fillingDate.search(/2018-06/) !=-1;

            }

            var six = $filter('filter')($scope.alerts, exp6);

            function exp7(item) {
                return item.fillingDate.search(/2018-07/) !=-1;

            }

            var seven = $filter('filter')($scope.alerts, exp7);

            function exp8(item) {
                return item.fillingDate.search(/2018-08/) !=-1;

            }

            var eight = $filter('filter')($scope.alerts, exp8);

            function exp9(item) {
                return item.fillingDate.search(/2018-09/) !=-1;

            }

            var nine = $filter('filter')($scope.alerts, exp9);

            function exp10(item) {
                return item.fillingDate.search(/2018-10/) !=-1;

            }

            var ten = $filter('filter')($scope.alerts, exp10);

            function exp11(item) {
                return item.fillingDate.search(/2018-11/) !=-1;

            }

            var eleven = $filter('filter')($scope.alerts, exp11);

            function exp12(item) {
                return item.fillingDate.search(/2018-12/) !=-1;

            }

            var twelve = $filter('filter')($scope.alerts, exp12);


            $scope.data = [inDanger.length, inDangerNot.length];
            $scope.data2 = [sent.length, progress.length,done.length, cancelled.length];
            $scope.data4 = [uno.length, due.length,tre.length, quatro.length];
            $scope.data3 = [one.length, two.length,three.length, four.length,five.length, six.length,seven.length, eight.length,nine.length, ten.length,eleven.length, twelve.length];
        }
        function onError(error) {
        }

        function expression(item) {
            return item.lifeDanger ==true;

        }
        function expression2(item) {
            return item.lifeDanger !=true;

        }
        function expression3(item) {
            return item.incidentStatus =='SENT';

        }
        function expression4(item) {
            return item.incidentStatus =='IN_PROGRESS';

        }
        function expression5(item) {
            return item.incidentStatus =='DONE';

        }
        function expression6(item) {
            return item.incidentStatus =='CANCELLED';

        }

        function expression11(item) {
            return item.howManyVictims ==1;

        }
        function expression12(item) {
            return item.howManyVictims >1 && item.howManyVictims <=5;

        }
        function expression13(item) {
            return item.howManyVictims >5 && item.howManyVictims <=10;

        }
        function expression14(item) {
            return item.howManyVictims >10;

        }
        $scope.chosen = 0;
        vm.refresh= function(id){
            $scope.chosen = id;
        }


        JhiConfigurationService.get().then(function(configuration) {
            vm.configuration = configuration;
        });
        JhiConfigurationService.getEnv().then(function (configuration) {
            vm.allConfiguration = configuration;
        });
    }
})();
