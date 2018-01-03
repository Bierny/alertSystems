(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('TestDabDialogController', TestDabDialogController);

    TestDabDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TestDab'];

    function TestDabDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TestDab) {
        var vm = this;

        vm.testDab = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.testDab.id !== null) {
                TestDab.update(vm.testDab, onSaveSuccess, onSaveError);
            } else {
                TestDab.save(vm.testDab, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('alertSystemApp:testDabUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.second = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
