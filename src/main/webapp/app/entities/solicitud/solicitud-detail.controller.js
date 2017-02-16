(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('SolicitudDetailController', SolicitudDetailController);

    SolicitudDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Solicitud', 'Frecuencia', 'Estado', 'Tipo'];

    function SolicitudDetailController($scope, $rootScope, $stateParams, previousState, entity, Solicitud, Frecuencia, Estado, Tipo) {
        var vm = this;

        vm.solicitud = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('examen2App:solicitudUpdate', function(event, result) {
            vm.solicitud = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
