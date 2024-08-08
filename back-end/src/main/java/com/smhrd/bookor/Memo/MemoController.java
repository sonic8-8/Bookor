package com.smhrd.bookor.Memo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {

    @Autowired
    private MemoService service;

    @GetMapping("/memo/listup")
    public List<Memo> listup(@RequestBody MemoDTO memoDTO) {

        Memo memo = Memo.builder()
                        .bookTitle(memoDTO.getBookTitle())
                        .build();

        List<Memo> memoList = service.listup(memo);

        return memoList;
    }

    @GetMapping("/memo/add")
    public void add(@PathVariable Long id, @RequestBody MemoDTO memoDTO) {
        Memo memo = Memo.builder()
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
