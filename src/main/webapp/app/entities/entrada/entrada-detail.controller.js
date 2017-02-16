(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('EntradaDetailController', EntradaDetailController);

    EntradaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Entrada', 'Beneficio', 'Tipo'];

    function EntradaDetailController($scope, $rootScope, $stateParams, previousState, entity, Entrada, Beneficio, Tipo) {
        var vm = this;

        vm.entrada = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('examen2App:entradaUpdate', function(event, result) {
            vm.entrada = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
