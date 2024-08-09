package com.smhrd.bookor.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public Book findById(Long id) {
        return repository.findById(id).get();
    }
    public void save(Book book){
        repository.save(book);
    }

    public List<Book> findBooksByUserId(String userId) {
        return repository.findByUserId(userId);
    }

}
