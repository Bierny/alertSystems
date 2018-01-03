(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentDetailController', IncidentDetailController);

    IncidentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Incident'];

    function IncidentDetailController($scope, $rootScope, $stateParams, previousState, entity, Incident) {
        var vm = this;

        vm.incident = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('alertSystemApp:incidentUpdate', function(event, result) {
            vm.incident = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
