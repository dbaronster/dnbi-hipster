package com.dnbi.hipster.repository.search;

import com.dnbi.hipster.domain.Author;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Author entity.
 */
public interface AuthorSearchRepository extends ElasticsearchRepository<Author, Long> {
}
