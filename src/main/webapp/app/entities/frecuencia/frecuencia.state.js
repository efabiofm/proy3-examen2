(function() {
    'use strict';

    angular
        .module('examen2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frecuencia', {
            parent: 'entity',
            url: '/frecuencia',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'examen2App.frecuencia.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frecuencia/frecuencias.html',
                    controller: 'FrecuenciaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frecuencia');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frecuencia-detail', {
            parent: 'frecuencia',
            url: '/frecuencia/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'examen2App.frecuencia.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frecuencia/frecuencia-detail.html',
                    controller: 'FrecuenciaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frecuencia');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Frecuencia', function($stateParams, Frecuencia) {
                    return Frecuencia.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frecuencia',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frecuencia-detail.edit', {
            parent: 'frecuencia-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frecuencia/frecuencia-dialog.html',
                    controller: 'FrecuenciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Frecuencia', function(Frecuencia) {
                            return Frecuencia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frecuencia.new', {
            parent: 'frecuencia',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frecuencia/frecuencia-dialog.html',
                    controller: 'FrecuenciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frecuencia', null, { reload: 'frecuencia' });
                }, function() {
                    $state.go('frecuencia');
                });
            }]
        })
        .state('frecuencia.edit', {
            parent: 'frecuencia',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frecuencia/frecuencia-dialog.html',
                    controller: 'FrecuenciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Frecuencia', function(Frecuencia) {
                            return Frecuencia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frecuencia', null, { reload: 'frecuencia' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frecuencia.delete', {
            parent: 'frecuencia',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frecuencia/frecuencia-delete-dialog.html',
                    controller: 'FrecuenciaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Frecuencia', function(Frecuencia) {
                            return Frecuencia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frecuencia', null, { reload: 'frecuencia' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
