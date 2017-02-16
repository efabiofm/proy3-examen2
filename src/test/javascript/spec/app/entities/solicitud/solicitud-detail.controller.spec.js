'use strict';

describe('Controller Tests', function() {

    describe('Solicitud Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSolicitud, MockFrecuencia, MockEstado, MockTipo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSolicitud = jasmine.createSpy('MockSolicitud');
            MockFrecuencia = jasmine.createSpy('MockFrecuencia');
            MockEstado = jasmine.createSpy('MockEstado');
            MockTipo = jasmine.createSpy('MockTipo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Solicitud': MockSolicitud,
                'Frecuencia': MockFrecuencia,
                'Estado': MockEstado,
                'Tipo': MockTipo
            };
            createController = function() {
                $injector.get('$controller')("SolicitudDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'examen2App:solicitudUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
