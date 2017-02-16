(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('SolicitudDeleteController',SolicitudDeleteController);

    SolicitudDeleteController.$inject = ['$uibModalInstance', 'entity', 'Solicitud'];

    function SolicitudDeleteController($uibModalInstance, entity, Solicitud) {
        var vm = this;

        vm.solicitud = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Solicitud.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
