package com.example.demo_api_movie.CommentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentDto {
    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "User name cannot be blank")
    @Size(max = 100, message = "User name must be at most 100 characters")
    private String userName;

    @NotBlank(message = "Avatar URL cannot be blank")
    @Size(max = 255, message = "Avatar URL must be at most 255 characters")
    private String avatarUrl;

    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 1000, message = "Comment content must not exceed 1000 characters")
    private String content;

    @Valid
    private List<AttachmentDto> attachments;
}
