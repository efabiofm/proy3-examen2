(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('FrecuenciaDeleteController',FrecuenciaDeleteController);

    FrecuenciaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Frecuencia'];

    function FrecuenciaDeleteController($uibModalInstance, entity, Frecuencia) {
        var vm = this;

        vm.frecuencia = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Frecuencia.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
