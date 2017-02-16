(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('BeneficioDetailController', BeneficioDetailController);

    BeneficioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Beneficio'];

    function BeneficioDetailController($scope, $rootScope, $stateParams, previousState, entity, Beneficio) {
        var vm = this;

        vm.beneficio = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('examen2App:beneficioUpdate', function(event, result) {
            vm.beneficio = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
