package com.smhrd.bookor.Memo;

import com.smhrd.bookor.Book.Book;
import com.smhrd.bookor.Book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {

    @Autowired
    private MemoService service;
    @Autowired
    private BookService bookService;

    @GetMapping("/memo/listup")
    public List<Memo> listup(@RequestParam("bookId") Long bookId) {
        List<Memo> memoList = service.findByBookId(bookId);
        return memoList;
    }

    @PostMapping("/memo/add") // @GetMapping -> @PostMapping 변경
    public void add(@RequestBody MemoDTO memoDTO) {
        Book book = bookService.findById(memoDTO.getBookId()); // 책 정보 조회 (예시)

        Memo memo = Memo.builder()
                .bookId(book.getBookId()) // 책 정보 추가
                .memoDate(memoDTO.getMemoDate())
                .memoPages(memoDTO.getMemoPages())
                .memoContent(memoDTO.getMemoContent())
                .build();

        service.save(memo);
    }


    @PutMapping("/memo/update/{id}")
    public void update(@PathVariable Long id, @RequestBody MemoDTO memoDTO) {
        Memo memo = service.findById(id); // 메모 조회
        if (memo != null) {
            memo.setMemoDate(memoDTO.getMemoDate());
            memo.setMemoPages(memoDTO.getMemoPages());
            memo.setMemoContent(memoDTO.getMemoContent());
            service.save(memo); // 수정된 메모 저장
        } else {
            // 메모를 찾지 못한 경우
        }
    }
}
