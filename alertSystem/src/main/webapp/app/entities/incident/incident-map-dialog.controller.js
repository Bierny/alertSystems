(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentMapController',IncidentMapController);

    IncidentMapController.$inject = ['$uibModalInstance', 'entity', 'TestDab','$state','$stateParams','$scope'];

    function IncidentMapController($uibModalInstance, entity, TestDab,$state,$stateParams,$scope) {
        var vm = this;

        vm.incident = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        $scope.loc = $stateParams.loc;

        function confirmDelete (id) {
            TestDab.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    $state.go('');
                });
        }
    }
})();
