package by.touchme.commentservice.mapper;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment dtoToModel(CommentDto commentDto);

    CommentDto modelToDto(Comment comment);

    List<CommentDto> toListDto(List<Comment> commentList);
}
