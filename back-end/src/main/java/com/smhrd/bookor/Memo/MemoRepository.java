package com.smhrd.bookor.Memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByBookId(Long bookId);

}
