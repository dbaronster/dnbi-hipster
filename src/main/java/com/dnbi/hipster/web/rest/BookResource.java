package com.dnbi.hipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dnbi.hipster.domain.Book;
import com.dnbi.hipster.service.BookService;
import com.dnbi.hipster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Book.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);
        
    @Inject
    private BookService bookService;
    
    /**
     * POST  /books : Create a new book.
     *
     * @param book the book to create
     * @return the ResponseEntity with status 201 (Created) and with body the new book, or with status 400 (Bad Request) if the book has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Book> createBook(@RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);
        if (book.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("book", "idexists", "A new book cannot already have an ID")).body(null);
        }
        Book result = bookService.save(book);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("book", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /books : Updates an existing book.
     *
     * @param book the book to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated book,
     * or with status 400 (Bad Request) if the book is not valid,
     * or with status 500 (Internal Server Error) if the book couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Book> updateBook(@RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to update Book : {}", book);
        if (book.getId() == null) {
            return createBook(book);
        }
        Book result = bookService.save(book);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("book", book.getId().toString()))
            .body(result);
    }

    /**
     * GET  /books : get all the books.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of books in body
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Book> getAllBooks() {
        log.debug("REST request to get all Books");
        return bookService.findAll();
    }

    /**
     * GET  /books/:id : get the "id" book.
     *
     * @param id the id of the book to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the book, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/books/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        Book book = bookService.findOne(id);
        return Optional.ofNullable(book)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /books/:id : delete the "id" book.
     *
     * @param id the id of the book to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/books/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("book", id.toString())).build();
    }

    /**
     * SEARCH  /_search/books?query=:query : search for the book corresponding
     * to the query.
     *
     * @param query the query of the book search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/books",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Book> searchBooks(@RequestParam String query) {
        log.debug("REST request to search Books for query {}", query);
        return bookService.search(query);
    }


}
