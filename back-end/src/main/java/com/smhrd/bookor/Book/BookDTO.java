package com.smhrd.bookor.Book;

import lombok.Data;

@Data
public class BookDTO {

    private String userId;
    private Long bookId;
    private String bookTitle;
    private int bookPages;
    private String bookReview;

}
