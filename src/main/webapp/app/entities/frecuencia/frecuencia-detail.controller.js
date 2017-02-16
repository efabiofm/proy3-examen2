(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('FrecuenciaDetailController', FrecuenciaDetailController);

    FrecuenciaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Frecuencia'];

    function FrecuenciaDetailController($scope, $rootScope, $stateParams, previousState, entity, Frecuencia) {
        var vm = this;

        vm.frecuencia = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('examen2App:frecuenciaUpdate', function(event, result) {
            vm.frecuencia = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
