package com.example.demo_api_movie.CommentService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentDto {
    @NotNull(message = "Attachment type cannot be null")
    private AttachmentType type;

    @NotBlank(message = "Attachment URL cannot be blank")
    @Size(max = 500, message = "Attachment URL must be at most 500 characters")
    private String url;
}
