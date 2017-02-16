(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('TipoDialogController', TipoDialogController);

    TipoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tipo'];

    function TipoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tipo) {
        var vm = this;

        vm.tipo = entity;
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
            if (vm.tipo.id !== null) {
                Tipo.update(vm.tipo, onSaveSuccess, onSaveError);
            } else {
                Tipo.save(vm.tipo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('examen2App:tipoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
