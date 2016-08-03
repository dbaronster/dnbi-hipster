(function() {
    'use strict';
    angular
        .module('dnbihipsterApp')
        .factory('Book', Book);

    Book.$inject = ['$resource', 'DateUtils'];

    function Book ($resource, DateUtils) {
        var resourceUrl =  'api/books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.pubDate = DateUtils.convertLocalDateFromServer(data.pubDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.pubDate = DateUtils.convertLocalDateToServer(data.pubDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.pubDate = DateUtils.convertLocalDateToServer(data.pubDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
