package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public static CommentDto consCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreateTime())
                .build();
    }
}
