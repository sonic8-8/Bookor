package com.smhrd.bookor.Book;

import com.smhrd.bookor.Memo.Memo;
import com.smhrd.bookor.Memo.MemoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping("/book/listup")
    public List<Book> listup(Principal principal) {
        String userId = principal.getName(); // 사용자 ID 가져오기
        List<Book> bookList = service.findBooksByUserId(userId); // 사용자 ID로 책 목록 조회
        return bookList;
    }

    @PostMapping("/book/add")
    public void add(@Validated @RequestBody BookDTO bookDTO) {
        Book book = Book.builder()
                .bookTitle(bookDTO.getBookTitle())
                .bookPages(bookDTO.getBookPages())
                .bookReview(bookDTO.getBookReview())
                .build();
        service.save(book);
    }

//    책 검색 기능
//    @GetMapping("/book/listup/{title}")
//    public List<Book> search(@PathVariable String title) {
//
//    }

}
