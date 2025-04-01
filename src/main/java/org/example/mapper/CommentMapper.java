package org.example.mapper;

import org.example.dto.CommentDto;
import org.example.interfaces.Mapper;
import org.example.model.Comment;

public class CommentMapper {

    public CommentMapper() {}

    public static CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        return dto;
    }

    public static Comment toEntity(CommentDto dto) {
        Comment comment = new Comment();
        return comment;
    }

}
