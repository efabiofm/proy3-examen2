(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('TipoDetailController', TipoDetailController);

    TipoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tipo'];

    function TipoDetailController($scope, $rootScope, $stateParams, previousState, entity, Tipo) {
        var vm = this;

        vm.tipo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('examen2App:tipoUpdate', function(event, result) {
            vm.tipo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
