(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .controller('IncidentController', IncidentController);

    IncidentController.$inject = ['Incident'];

    function IncidentController(Incident) {

        var vm = this;

        vm.incidents = [];

        loadAll();

        function loadAll() {
            Incident.query(function(result) {
                vm.incidents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
