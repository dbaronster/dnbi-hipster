package com.dnbi.hipster.service.impl;

import com.dnbi.hipster.service.AuthorService;
import com.dnbi.hipster.domain.Author;
import com.dnbi.hipster.repository.AuthorRepository;
import com.dnbi.hipster.repository.search.AuthorSearchRepository;
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
 * Service Implementation for managing Author.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService{

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);
    
    @Inject
    private AuthorRepository authorRepository;
    
    @Inject
    private AuthorSearchRepository authorSearchRepository;
    
    /**
     * Save a author.
     * 
     * @param author the entity to save
     * @return the persisted entity
     */
    public Author save(Author author) {
        log.debug("Request to save Author : {}", author);
        Author result = authorRepository.save(author);
        authorSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the authors.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Author> findAll() {
        log.debug("Request to get all Authors");
        List<Author> result = authorRepository.findAll();
        return result;
    }

    /**
     *  Get one author by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Author findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        Author author = authorRepository.findOne(id);
        return author;
    }

    /**
     *  Delete the  author by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.delete(id);
        authorSearchRepository.delete(id);
    }

    /**
     * Search for the author corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Author> search(String query) {
        log.debug("Request to search Authors for query {}", query);
        return StreamSupport
            .stream(authorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
