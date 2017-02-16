(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('TipoController', TipoController);

    TipoController.$inject = ['Tipo'];

    function TipoController(Tipo) {
        var vm = this;

        vm.tipos = [];

        loadAll();

        function loadAll() {
            Tipo.query(function(result) {
                vm.tipos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
