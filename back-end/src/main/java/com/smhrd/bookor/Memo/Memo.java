package com.smhrd.bookor.Memo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name="tb_memo")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name="memo_date")
    private Date memoDate;

    @Column(name="memo_pages")
    private int memoPages;

    @Column(name = "memo_content")
    private String memoContent;
}
