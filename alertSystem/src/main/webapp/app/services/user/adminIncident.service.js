(function () {
    'use strict';

    angular
        .module('alertSystemApp')
        .factory('AdminIncidents', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/adminIncidents/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();
