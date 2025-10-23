package com.example.demo_api_movie.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController  {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public Comment findById(@PathVariable Long id){
        return commentService.getCommentById(id);
    }

    @PostMapping
    public Comment newComment(@Valid @RequestBody CreateCommentDto createCommentDto){
        return commentService.createComment(createCommentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        commentService.deleteComment(id);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Page<Comment>> getComments(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Comment> comments = commentService.getCommentsByMovieId(movieId, page, size);
        return ResponseEntity.ok(comments);
    }
}
