package by.touchme.commentservice.controller;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.dto.PageDto;
import by.touchme.commentservice.dto.SearchDto;
import by.touchme.commentservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/comment")
@RequiredArgsConstructor
@RestController
public class SearchCommentController {

    private final CommentService commentService;

    @GetMapping("/search")
    public ResponseEntity<PageDto<CommentDto>> getPage(@Valid @RequestBody SearchDto searchDto, Pageable pageable) {
        return new ResponseEntity<>(commentService.getPageByCriteria(searchDto, pageable), HttpStatus.OK);
    }
}
