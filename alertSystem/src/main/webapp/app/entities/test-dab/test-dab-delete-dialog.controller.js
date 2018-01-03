(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('TestDabDeleteController',TestDabDeleteController);

    TestDabDeleteController.$inject = ['$uibModalInstance', 'entity', 'TestDab'];

    function TestDabDeleteController($uibModalInstance, entity, TestDab) {
        var vm = this;

        vm.testDab = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TestDab.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
