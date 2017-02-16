(function() {
    'use strict';
    angular
        .module('examen2App')
        .factory('Solicitud', Solicitud);

    Solicitud.$inject = ['$resource', 'DateUtils'];

    function Solicitud ($resource, DateUtils) {
        var resourceUrl =  'api/solicituds/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaEntrega = DateUtils.convertDateTimeFromServer(data.fechaEntrega);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
