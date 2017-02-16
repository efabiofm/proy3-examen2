(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('FrecuenciaController', FrecuenciaController);

    FrecuenciaController.$inject = ['Frecuencia'];

    function FrecuenciaController(Frecuencia) {
        var vm = this;

        vm.frecuencias = [];

        loadAll();

        function loadAll() {
            Frecuencia.query(function(result) {
                vm.frecuencias = result;
                vm.searchQuery = null;
            });
        }
    }
})();
