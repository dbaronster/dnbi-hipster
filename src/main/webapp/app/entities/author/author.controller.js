(function() {
    'use strict';

    angular
        .module('dnbihipsterApp')
        .controller('AuthorController', AuthorController);

    AuthorController.$inject = ['$scope', '$state', 'Author', 'AuthorSearch'];

    function AuthorController ($scope, $state, Author, AuthorSearch) {
        var vm = this;
        
        vm.authors = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Author.query(function(result) {
                vm.authors = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AuthorSearch.query({query: vm.searchQuery}, function(result) {
                vm.authors = result;
            });
        }    }
})();
