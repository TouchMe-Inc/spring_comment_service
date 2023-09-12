package by.touchme.commentservice.controller;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.dto.PageDto;
import by.touchme.commentservice.service.CommentService;
import by.touchme.commentservice.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


/**
 * CRUD controller for comments.
 */
@RequestMapping("/v1/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final PermissionService permissionService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(commentService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageDto<CommentDto>> getPage(Pageable pageable) {
        return new ResponseEntity<>(commentService.getPage(pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto comment) {
        CommentDto createdComment = commentService.create(comment);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        permissionService.addPermissionForUser(createdComment, BasePermission.DELETE, username);
        permissionService.addPermissionForUser(createdComment, BasePermission.WRITE, username);

        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasPermission(#id, 'by.touchme.commentservice.dto.CommentDto', 'WRITE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentDto> updateById(
            @PathVariable(name = "id") Long id, @Valid @RequestBody CommentDto comment) {
        return new ResponseEntity<>(commentService.updateById(id, comment), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasPermission(#id, 'by.touchme.commentservice.dto.CommentDto', 'DELETE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        commentService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
