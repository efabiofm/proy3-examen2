(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('EstadoDetailController', EstadoDetailController);

    EstadoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Estado'];

    function EstadoDetailController($scope, $rootScope, $stateParams, previousState, entity, Estado) {
        var vm = this;

        vm.estado = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('examen2App:estadoUpdate', function(event, result) {
            vm.estado = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
