(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('FrecuenciaDialogController', FrecuenciaDialogController);

    FrecuenciaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Frecuencia'];

    function FrecuenciaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Frecuencia) {
        var vm = this;

        vm.frecuencia = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.frecuencia.id !== null) {
                Frecuencia.update(vm.frecuencia, onSaveSuccess, onSaveError);
            } else {
                Frecuencia.save(vm.frecuencia, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('examen2App:frecuenciaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
