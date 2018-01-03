(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentDialogController', IncidentDialogController);

    IncidentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Incident'];

    function IncidentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Incident) {
        var vm = this;

        vm.incident = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.incident.id !== null) {
                Incident.update(vm.incident, onSaveSuccess, onSaveError);
            } else {
                Incident.save(vm.incident, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('alertSystemApp:incidentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
