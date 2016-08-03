package com.dnbi.hipster.service.impl;

import com.dnbi.hipster.service.BookService;
import com.dnbi.hipster.domain.Book;
import com.dnbi.hipster.repository.BookRepository;
import com.dnbi.hipster.repository.search.BookSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Book.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService{

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    
    @Inject
    private BookRepository bookRepository;
    
    @Inject
    private BookSearchRepository bookSearchRepository;
    
    /**
     * Save a book.
     * 
     * @param book the entity to save
     * @return the persisted entity
     */
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);
        Book result = bookRepository.save(book);
        bookSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the books.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Book> findAll() {
        log.debug("Request to get all Books");
        List<Book> result = bookRepository.findAll();
        return result;
    }

    /**
     *  Get one book by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Book findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOne(id);
        return book;
    }

    /**
     *  Delete the  book by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(id);
        bookSearchRepository.delete(id);
    }

    /**
     * Search for the book corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Book> search(String query) {
        log.debug("Request to search Books for query {}", query);
        return StreamSupport
            .stream(bookSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
