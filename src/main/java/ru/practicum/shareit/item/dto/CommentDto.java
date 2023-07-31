package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public static CommentDto consCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreateTime());
        return commentDto;
    }
}
