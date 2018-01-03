(function() {
    'use strict';
    angular
        .module('alertSystemApp')
        .factory('TestDab', TestDab);

    TestDab.$inject = ['$resource', 'DateUtils'];

    function TestDab ($resource, DateUtils) {
        var resourceUrl =  'api/test-dabs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.second = DateUtils.convertLocalDateFromServer(data.second);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.second = DateUtils.convertLocalDateToServer(copy.second);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.second = DateUtils.convertLocalDateToServer(copy.second);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
