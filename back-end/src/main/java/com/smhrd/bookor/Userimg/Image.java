package com.smhrd.bookor.Userimg;

import com.smhrd.bookor.member.Member;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userimg_id")
    private Long userimgId;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "data", columnDefinition = "BLOB")
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;
}

