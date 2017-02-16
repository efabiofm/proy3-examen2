(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('BeneficioController', BeneficioController);

    BeneficioController.$inject = ['Beneficio'];

    function BeneficioController(Beneficio) {
        var vm = this;

        vm.beneficios = [];

        loadAll();

        function loadAll() {
            Beneficio.query(function(result) {
                vm.beneficios = result;
                vm.searchQuery = null;
            });
        }
    }
})();
