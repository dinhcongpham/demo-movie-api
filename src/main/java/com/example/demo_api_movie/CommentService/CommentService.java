package com.example.demo_api_movie.CommentService;


import com.example.demo_api_movie.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;


    @Transactional
    public Comment createComment(CreateCommentDto dto) {
        // call user service to verify
        // call movie service to verify

        Comment comment = toEntity(dto);

        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<Attachment> attachments = dto.getAttachments().stream()
                    .map(this::toEntity)
                    .peek(a -> a.setComment(comment))
                    .toList();

            comment.setAttachments(attachments);
        }

        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.getCommentByID(id)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment not found");
        }
        
        commentRepository.deleteById(id);
    }

    public Page<Comment> getCommentsByMovieId(Long movieId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepository.findByMovieIdOrderByCreatedAtDesc(movieId, pageable);
    }

    private Comment toEntity(CreateCommentDto createCommentDto) {
        return Comment.builder()
                .movieId(createCommentDto.getMovieId())
                .userId(createCommentDto.getUserId())
                .content(createCommentDto.getContent())
                .userName(createCommentDto.getUserName())
                .avatarUrl(createCommentDto.getAvatarUrl())
                .build();
    }

    private Attachment toEntity(AttachmentDto attachmentDto) {
        return Attachment.builder()
                .type(attachmentDto.getType())
                .url(attachmentDto.getUrl())
                .build();
    }
}
