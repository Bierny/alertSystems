(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('TestDabDetailController', TestDabDetailController);

    TestDabDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TestDab'];

    function TestDabDetailController($scope, $rootScope, $stateParams, previousState, entity, TestDab) {
        var vm = this;

        vm.testDab = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('alertSystemApp:testDabUpdate', function(event, result) {
            vm.testDab = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
