package by.touchme.commentservice.service.impl;

import by.touchme.commentservice.criteria.SearchCriteria;
import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.dto.PageDto;
import by.touchme.commentservice.dto.SearchDto;
import by.touchme.commentservice.entity.Comment;
import by.touchme.commentservice.exception.CommentNotFoundException;
import by.touchme.commentservice.mapper.CommentMapper;
import by.touchme.commentservice.repository.CommentRepository;
import by.touchme.commentservice.service.CommentService;
import by.touchme.commentservice.specification.CommentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Cacheable(cacheNames = "comments", key = "#id")
    @Override
    public CommentDto getById(Long id) {
        log.info("Get comment with id = {}", id);
        return commentMapper.modelToDto(
                commentRepository
                        .findById(id)
                        .orElseThrow(() -> new CommentNotFoundException(id))
        );
    }

    @Override
    public PageDto<CommentDto> getPageByCriteria(SearchDto search, Pageable pageable) {
        log.info("Get comment page ({}) by criteria {}", pageable, search);

        List<SearchCriteria> criteriaList = search.getCriteriaList();

        Specification<Comment> specification = null;

        if (!criteriaList.isEmpty()) {
            specification = new CommentSpecification(criteriaList.get(0));
            for (int idx = 1; idx < criteriaList.size(); idx++) {
                SearchCriteria criteria = criteriaList.get(idx);
                specification = Specification.where(specification).and(new CommentSpecification(criteria));
            }
        }

        Page<Comment> page = commentRepository.findAll(specification, pageable);

        return new PageDto<>(page.map(commentMapper::modelToDto));
    }

    @Override
    public PageDto<CommentDto> getPage(Pageable pageable) {
        log.info("Get comment page ({})", pageable);
        Page<Comment> page = commentRepository.findAll(pageable);

        return new PageDto<>(page.map(commentMapper::modelToDto));
    }

    @Override
    public PageDto<CommentDto> getPageByNewsId(Long newsId, Pageable pageable) {
        log.info("Get comment page ({}) with news_id = {}", pageable, newsId);
        Page<Comment> page = commentRepository.findAllByNewsId(newsId, pageable);

        return new PageDto<>(page.map(commentMapper::modelToDto));
    }

    @CachePut(cacheNames = "comments", key = "#result.id")
    @Override
    public CommentDto create(CommentDto comment) {
        log.info("Create comment ({})", comment);
        return commentMapper.modelToDto(
                commentRepository.save(
                        commentMapper.dtoToModel(comment)
                )
        );
    }

    @CachePut(cacheNames = "comments", key = "#id")
    @Override
    public CommentDto updateById(Long id, CommentDto comment) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }

        comment.setId(id);

        log.info("Update comment with id = {} ({})", id, comment);
        return commentMapper.modelToDto(
                commentRepository.save(
                        commentMapper.dtoToModel(comment)
                )
        );
    }

    @CacheEvict(cacheNames = "comments", key = "#id")
    @Override
    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }

        log.info("Delete comment with id = {}", id);
        commentRepository.deleteById(id);
    }
}
