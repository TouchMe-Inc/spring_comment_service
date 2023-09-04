package by.touchme.commentservice.controller;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.dto.PageDto;
import by.touchme.commentservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CRUD controller for comments.
 */
@RequestMapping("/v1/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @Cacheable(cacheNames = "comments", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(commentService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageDto<CommentDto>> getPage(Pageable pageable) {
        return new ResponseEntity<>(commentService.getPage(pageable), HttpStatus.OK);
    }

    @CachePut(cacheNames = "comments", key = "#result.body.id")
    @PostMapping
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto comment) {
        return new ResponseEntity<>(commentService.create(comment), HttpStatus.CREATED);
    }

    @CachePut(cacheNames = "comments", key = "#id")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentDto> updateById(
            @PathVariable(name = "id") Long id, @Valid @RequestBody CommentDto comment) {
        return new ResponseEntity<>(commentService.updateById(id, comment), HttpStatus.NO_CONTENT);
    }

    @CacheEvict(cacheNames = "comments", key = "#id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        commentService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
