package com.smhrd.bookor.Userimg;

import com.smhrd.bookor.member.Member;
import com.smhrd.bookor.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Image saveOrUpdateImage(Long userId, MultipartFile file) throws IOException {
        Optional<Member> memberOptional = memberRepository.findById(userId);
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found with userId: " + userId);
        }
        Member member = memberOptional.get();

        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .data(file.getBytes())
                .member(member)
                .build();

        return imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public ImageDTO getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
        return new ImageDTO(image.getUserimgId(), image.getFileName(), image.getData());
    }
}
