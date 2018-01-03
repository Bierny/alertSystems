(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('test-dab', {
            parent: 'entity',
            url: '/test-dab?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alertSystemApp.testDab.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test-dab/test-dabs.html',
                    controller: 'TestDabController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('testDab');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('test-dab-detail', {
            parent: 'test-dab',
            url: '/test-dab/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alertSystemApp.testDab.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/test-dab/test-dab-detail.html',
                    controller: 'TestDabDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('testDab');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TestDab', function($stateParams, TestDab) {
                    return TestDab.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'test-dab',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('test-dab-detail.edit', {
            parent: 'test-dab-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-dab/test-dab-dialog.html',
                    controller: 'TestDabDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TestDab', function(TestDab) {
                            return TestDab.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test-dab.new', {
            parent: 'test-dab',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-dab/test-dab-dialog.html',
                    controller: 'TestDabDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                second: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('test-dab', null, { reload: 'test-dab' });
                }, function() {
                    $state.go('test-dab');
                });
            }]
        })
        .state('test-dab.edit', {
            parent: 'test-dab',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-dab/test-dab-dialog.html',
                    controller: 'TestDabDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TestDab', function(TestDab) {
                            return TestDab.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test-dab', null, { reload: 'test-dab' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('test-dab.delete', {
            parent: 'test-dab',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/test-dab/test-dab-delete-dialog.html',
                    controller: 'TestDabDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TestDab', function(TestDab) {
                            return TestDab.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('test-dab', null, { reload: 'test-dab' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
