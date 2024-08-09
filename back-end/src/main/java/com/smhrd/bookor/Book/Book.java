package com.smhrd.bookor.Book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_book")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_pages")
    private int bookPages;

    @Column(name = "book_review")
    private String bookReview;

}
