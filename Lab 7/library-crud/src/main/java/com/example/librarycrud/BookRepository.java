package com.example.librarycrud;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByAuthorIgnoreCase(String author);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByTitleContainingIgnoreCase(String keyword);

    List<Book> findByCopiesGreaterThan(Integer copies);
}