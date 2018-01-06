(function() {
    'use strict';

    angular
        .module('alertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        })
            .state('incident-detail', {
                parent: 'home',
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
                    entity: ['$stateParams', 'TestDab', function($stateParams, TestDab) {
                        return TestDab.get({id : 1}).$promise;
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
            });
    }
})();
