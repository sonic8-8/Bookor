package com.smhrd.bookor.Userimg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("userId") Long userId) {
        try {
            Image savedImage = imageService.saveOrUpdateImage(userId, file);
            return new ResponseEntity<>("Image uploaded successfully: " + savedImage.getUserimgId(), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageDTO imageDto = imageService.getImage(id);
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(imageDto.getData());
    }
}
