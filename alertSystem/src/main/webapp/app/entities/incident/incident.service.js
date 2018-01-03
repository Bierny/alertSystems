(function() {
    'use strict';
    angular
        .module('alertSystemApp')
        .factory('Incident', Incident);

    Incident.$inject = ['$resource'];

    function Incident ($resource) {
        var resourceUrl =  'api/incidents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
