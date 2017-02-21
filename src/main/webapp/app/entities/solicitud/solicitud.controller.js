(function() {
    'use strict';

    angular
        .module('examen2App')
        .controller('SolicitudController', SolicitudController);

    SolicitudController.$inject = ['Solicitud', 'Estado'];


    function SolicitudController(Solicitud, Estado) {
        var vm = this;

        Estado.query().$promise.then(function (resultado){
            vm.estados = resultado;
        });
        vm.solicituds = [];
        // vm.estados = ["En proceso", "Listo para entrega", "Entregado"];

        loadAll();


        function loadAll() {
            Solicitud.query(function(result) {
                vm.solicituds = result;
                vm.searchQuery = null;
            });
        }
    }
})();
