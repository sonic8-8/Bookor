package com.smhrd.bookor.Book;

import lombok.Data;

@Data
public class BookDTO {

    private int bookId;
    private String bookTitle;
    private int bookPages;
    private String bookReview;

}
