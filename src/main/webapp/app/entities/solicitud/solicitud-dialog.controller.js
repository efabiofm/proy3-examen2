(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('SolicitudDialogController', SolicitudDialogController);

    SolicitudDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Solicitud', 'Frecuencia', 'Estado', 'Tipo'];

    function SolicitudDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Solicitud, Frecuencia, Estado, Tipo) {
        var vm = this;

        vm.solicitud = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.frecuencias = Frecuencia.query();
        vm.estados = Estado.query();
        vm.tipos = Tipo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.solicitud.id !== null) {
                Solicitud.update(vm.solicitud, onSaveSuccess, onSaveError);
            } else {
                Solicitud.save(vm.solicitud, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('examen2App:solicitudUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaEntrega = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
