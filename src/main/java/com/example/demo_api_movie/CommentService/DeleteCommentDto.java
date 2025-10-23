package com.example.demo_api_movie.CommentService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCommentDto {
    @NotBlank(message = "Comment ID cannot be blank")
    private String id;
}
