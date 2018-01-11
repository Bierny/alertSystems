(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentMapController',IncidentMapController);

    IncidentMapController.$inject = ['$uibModalInstance', 'entity', 'TestDab','$state','$stateParams','$scope','loc'];

    function IncidentMapController($uibModalInstance, entity, TestDab,$state,$stateParams,$scope,loc) {
        var vm = this;

        vm.incident = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        if($stateParams.loc) {
            $scope.loc = $stateParams.loc;
        }else{
            $scope.loc = loc;
        }
    console.log("tutaj ",$scope.loc);
        function confirmDelete (id) {
            TestDab.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    $state.go('');
                });
        }
    }
})();
