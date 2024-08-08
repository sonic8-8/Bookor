package com.smhrd.bookor.Memo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    @Autowired
    private MemoRepository repository;

    public List<Memo> listup(Memo memo){
        return repository.findAllByBookTitle(memo.getBookTitle());
    }

    public void save(Memo memo) {
        repository.save(memo);
    }

    public Memo findById(Long id) {
        return repository.findById(id).get();
    }
}
