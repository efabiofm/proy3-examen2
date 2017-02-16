(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('EntradaController', EntradaController);

    EntradaController.$inject = ['Entrada'];

    function EntradaController(Entrada) {
        var vm = this;

        vm.entradas = [];

        loadAll();

        function loadAll() {
            Entrada.query(function(result) {
                vm.entradas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
