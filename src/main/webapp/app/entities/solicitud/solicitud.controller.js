(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('SolicitudController', SolicitudController);

    SolicitudController.$inject = ['Solicitud'];

    function SolicitudController(Solicitud) {
        var vm = this;

        vm.solicituds = [];

        loadAll();

        function loadAll() {
            Solicitud.query(function(result) {
                vm.solicituds = result;
                vm.searchQuery = null;
            });
        }
    }
})();
