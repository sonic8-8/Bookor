package com.smhrd.bookor.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public List<Book> listup(Book book) {
        return repository.findAllByUserId(book.getUserId());
    }

    public void save(Book book){
        repository.save(book);
    }
}
