package com.Library.SpringShelf.Service; // You can place this in a 'specifications' sub-package

import com.Library.SpringShelf.Model.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    /**
     * Creates a specification to find books where the title contains the given string (case-insensitive).
     */
    public static Specification<Book> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    /**
     * Creates a specification to find books where the author's name contains the given string (case-insensitive).
     */
    public static Specification<Book> authorContains(String author) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }
}