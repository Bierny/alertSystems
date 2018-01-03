(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('incident', {
            parent: 'entity',
            url: '/incident',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alertSystemApp.incident.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incident/incidents.html',
                    controller: 'IncidentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('incident');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('incident-detail', {
            parent: 'incident',
            url: '/incident/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alertSystemApp.incident.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incident/incident-detail.html',
                    controller: 'IncidentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('incident');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Incident', function($stateParams, Incident) {
                    return Incident.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'incident',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('incident-detail.edit', {
            parent: 'incident-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incident/incident-dialog.html',
                    controller: 'IncidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Incident', function(Incident) {
                            return Incident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incident.new', {
            parent: 'incident',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incident/incident-dialog.html',
                    controller: 'IncidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                kind: null,
                                location: null,
                                serviceKind: null,
                                lifeDanger: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('incident', null, { reload: 'incident' });
                }, function() {
                    $state.go('incident');
                });
            }]
        })
        .state('incident.edit', {
            parent: 'incident',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incident/incident-dialog.html',
                    controller: 'IncidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Incident', function(Incident) {
                            return Incident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incident', null, { reload: 'incident' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incident.delete', {
            parent: 'incident',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incident/incident-delete-dialog.html',
                    controller: 'IncidentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Incident', function(Incident) {
                            return Incident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incident', null, { reload: 'incident' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
