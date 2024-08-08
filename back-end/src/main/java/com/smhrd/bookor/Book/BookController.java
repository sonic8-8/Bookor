package com.smhrd.bookor.Book;

import com.smhrd.bookor.Memo.Memo;
import com.smhrd.bookor.Memo.MemoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping("/book/listup")
    public List<Book> listup(@RequestBody BookDTO bookDTO) {

        Book book = Book.builder()
                .bookTitle(bookDTO.getBookTitle())
                .build();

        List<Book> BookList = service.listup(book);

        return BookList;
    }

    @GetMapping("/book/add")
    public void add(@RequestBody BookDTO bookDTO) {
        Book book = Book.builder()
                .bookTitle(bookDTO.getBookTitle())
                .bookPages(bookDTO.getBookPages())
                .bookReview(bookDTO.getBookReview())
                .build();

        service.save(book);
    }
}
