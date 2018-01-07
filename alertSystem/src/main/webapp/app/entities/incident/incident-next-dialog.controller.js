(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentNextController',IncidentNextController);

    IncidentNextController.$inject = ['$uibModalInstance', 'entity', 'TestDab','$state'];

    function IncidentNextController($uibModalInstance, entity, TestDab,$state) {
        var vm = this;

        vm.incident = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TestDab.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    $state.go('');
                });
        }
    }
})();
