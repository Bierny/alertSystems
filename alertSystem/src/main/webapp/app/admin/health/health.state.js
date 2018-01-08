(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('jhi-health', {
            parent: 'admin',
            url: '/health',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'health.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/health/health.html',
                    controller: 'JhiHealthCheckController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                return {
                    page: PaginationUtil.parsePage($stateParams.page),
                    sort: $stateParams.sort,
                    predicate: PaginationUtil.parsePredicate($stateParams.sort),
                    ascending: PaginationUtil.parseAscending($stateParams.sort)
                };
            }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
