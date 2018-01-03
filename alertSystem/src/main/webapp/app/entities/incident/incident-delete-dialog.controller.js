(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentDeleteController',IncidentDeleteController);

    IncidentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Incident'];

    function IncidentDeleteController($uibModalInstance, entity, Incident) {
        var vm = this;

        vm.incident = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Incident.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
