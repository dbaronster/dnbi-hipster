(function() {
    'use strict';

    angular
        .module('dnbihipsterApp')
        .controller('BookController', BookController);

    BookController.$inject = ['$scope', '$state', 'Book', 'BookSearch'];

    function BookController ($scope, $state, Book, BookSearch) {
        var vm = this;
        
        vm.books = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Book.query(function(result) {
                vm.books = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BookSearch.query({query: vm.searchQuery}, function(result) {
                vm.books = result;
            });
        }    }
})();
