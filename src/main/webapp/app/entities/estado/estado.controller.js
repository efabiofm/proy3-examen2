(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('EstadoController', EstadoController);

    EstadoController.$inject = ['Estado'];

    function EstadoController(Estado) {
        var vm = this;

        vm.estados = [];

        loadAll();

        function loadAll() {
            Estado.query(function(result) {
                vm.estados = result;
                vm.searchQuery = null;
            });
        }
    }
})();
