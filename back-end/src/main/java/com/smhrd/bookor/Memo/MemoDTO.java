package com.smhrd.bookor.Memo;

import lombok.Data;

import java.sql.Date;

@Data
public class MemoDTO {

    private String bookTitle;
    private Date memoDate;
    private int memoPages;
    private String memoContent;

}
