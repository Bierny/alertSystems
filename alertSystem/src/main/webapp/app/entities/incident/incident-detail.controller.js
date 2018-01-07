(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentDetailController', IncidentDetailController);

    IncidentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TestDab'];

    function IncidentDetailController($scope, $rootScope, $stateParams, previousState, entity, TestDab) {
        var vm = this;
        $scope.isCall = false;

        vm.incident = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('alertSystemApp:incidentUpdate', function(event, result) {
            vm.incident = result;
        });
        $scope.callNotifier=function(){
            $scope.isCall = true;
        }

        vm.incident.incidentStatus = 'IN_PROGRESS';
        if($rootScope.fromHome == true) {
            TestDab.update(vm.incident, onSaveSuccess2, onSaveError);
        }
        function onSaveSuccess2 (result) {
            $rootScope.fromHome = false;
        }

        function onSaveError (result) {
        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
