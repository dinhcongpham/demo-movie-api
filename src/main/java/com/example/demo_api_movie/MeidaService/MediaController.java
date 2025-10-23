package com.example.demo_api_movie.MeidaService;

import com.example.demo_api_movie.EpisodeService.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final S3Service s3Service;

    @PostMapping("/presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String fileName,
                                                       @RequestParam String contentType,
                                                       @RequestParam(defaultValue = "video") String type) {
        String presignedUrl = s3Service.generatePresignedUploadUrl(fileName, contentType, type);
        return ResponseEntity.ok(presignedUrl);
    }

    @PostMapping("/upload/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return s3Service.uploadFile(file, false);
    }

    @PostMapping("/upload/video")
    public String uploadVideo(@RequestParam("file") MultipartFile file) {
        return s3Service.uploadFile(file, true);
    }

    @DeleteMapping("/delete/{type}/{fileUrl}")
    public ResponseEntity<String> deleteFile(@PathVariable String type, @PathVariable  String fileUrl) {
        s3Service.deleteFile(fileUrl, type);
        return ResponseEntity.ok("Deleted successfully: " + fileUrl);
    }
}
