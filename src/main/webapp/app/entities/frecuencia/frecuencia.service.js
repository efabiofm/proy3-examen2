(function() {
    'use strict';
    angular
        .module('examen2App')
        .factory('Frecuencia', Frecuencia);

    Frecuencia.$inject = ['$resource'];

    function Frecuencia ($resource) {
        var resourceUrl =  'api/frecuencias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
