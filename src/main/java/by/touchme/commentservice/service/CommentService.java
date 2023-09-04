package by.touchme.commentservice.service;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.dto.PageDto;
import by.touchme.commentservice.dto.SearchDto;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto getById(Long id);

    PageDto<CommentDto> getPageByCriteria(SearchDto search, Pageable pageable);

    PageDto<CommentDto> getPage(Pageable pageable);

    PageDto<CommentDto> getPageByNewsId(Long newsId, Pageable pageable);

    CommentDto create(CommentDto comment);

    CommentDto updateById(Long id, CommentDto comment);

    void deleteById(Long id);
}
