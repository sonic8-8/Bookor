package com.smhrd.bookor.Userimg;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDTO {
    private Long memberId;
    private String fileName;
    private byte[] data;
}