package com.howtodoinjava.vaadincrud;

import com.howtodoinjava.vaadincrud.book.Book;
import com.howtodoinjava.vaadincrud.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void savedBookCanBeFoundByTitle() {
        bookRepository.save(new Book("Domain-Driven Design", "Eric Evans", true));

        List<Book> found = bookRepository.findByTitleContainingIgnoreCase("domain-driven");

        assertEquals(1, found.size());
        assertEquals("Eric Evans", found.get(0).getAuthor());
    }

    @Test
    void seedDataIsLoaded() {
        List<Book> all = bookRepository.findAll();

        assertTrue(all.size() >= 3);
    }
}
