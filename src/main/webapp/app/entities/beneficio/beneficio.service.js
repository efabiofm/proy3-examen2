(function() {
    'use strict';
    angular
        .module('examen2App')
        .factory('Beneficio', Beneficio);

    Beneficio.$inject = ['$resource'];

    function Beneficio ($resource) {
        var resourceUrl =  'api/beneficios/:id';

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
