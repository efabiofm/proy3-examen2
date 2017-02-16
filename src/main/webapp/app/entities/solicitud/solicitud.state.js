(function() {
    'use strict';

    angular
        .module('examen2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('solicitud', {
            parent: 'entity',
            url: '/solicitud',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'examen2App.solicitud.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/solicitud/solicituds.html',
                    controller: 'SolicitudController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('solicitud');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('solicitud-detail', {
            parent: 'solicitud',
            url: '/solicitud/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'examen2App.solicitud.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/solicitud/solicitud-detail.html',
                    controller: 'SolicitudDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('solicitud');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Solicitud', function($stateParams, Solicitud) {
                    return Solicitud.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'solicitud',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('solicitud-detail.edit', {
            parent: 'solicitud-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud/solicitud-dialog.html',
                    controller: 'SolicitudDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Solicitud', function(Solicitud) {
                            return Solicitud.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('solicitud.new', {
            parent: 'solicitud',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud/solicitud-dialog.html',
                    controller: 'SolicitudDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fechaEntrega: null,
                                esRecursivo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('solicitud', null, { reload: 'solicitud' });
                }, function() {
                    $state.go('solicitud');
                });
            }]
        })
        .state('solicitud.edit', {
            parent: 'solicitud',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud/solicitud-dialog.html',
                    controller: 'SolicitudDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Solicitud', function(Solicitud) {
                            return Solicitud.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('solicitud', null, { reload: 'solicitud' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('solicitud.delete', {
            parent: 'solicitud',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud/solicitud-delete-dialog.html',
                    controller: 'SolicitudDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Solicitud', function(Solicitud) {
                            return Solicitud.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('solicitud', null, { reload: 'solicitud' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
