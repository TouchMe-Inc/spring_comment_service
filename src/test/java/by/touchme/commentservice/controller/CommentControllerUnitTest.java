package by.touchme.commentservice.controller;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.filter.JwtFilter;
import by.touchme.commentservice.service.CommentService;
import by.touchme.commentservice.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CommentController.class)
public class CommentControllerUnitTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @MockBean
    JwtFilter jwtFilter;

    @MockBean
    PermissionService permissionService;

    @DisplayName("JUnit test for CommentController.getPage")
    @Test
    void getPage() throws Exception {
        mockMvc.perform(
                        get("/v1/comment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("JUnit test for CommentController.getById")
    @Test
    void getById() throws Exception {
        this.mockMvc
                .perform(
                        get("/v1/comment/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("JUnit test for CommentController.create")
    @WithMockUser
    @Test
    void create() throws Exception {
        CommentDto createComment = new CommentDto();
        createComment.setNewsId(1L);
        createComment.setUsername("John Doe");
        createComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        CommentDto createdComment = new CommentDto();
        createdComment.setNewsId(1L);
        createComment.setUsername("John Snow");
        createComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        createdComment.setTime(new Date());

        when(commentService.create(createComment)).thenReturn(createdComment);

        mockMvc.perform(
                        post("/v1/comment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createComment))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("JUnit test for CommentController.updateById")
    @WithMockUser
    @Test
    void updateById() throws Exception {
        CommentDto updateComment = new CommentDto();
        updateComment.setNewsId(1L);
        updateComment.setUsername("John Doe");
        updateComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        CommentDto updatedComment = new CommentDto();
        updatedComment.setNewsId(1L);
        updatedComment.setUsername("John Doe");
        updatedComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        updatedComment.setTime(new Date());

        when(commentService.updateById(any(), any())).thenReturn(updatedComment);

        mockMvc.perform(
                        put("/v1/comment/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateComment))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("JUnit test for CommentController.deleteById")
    @WithMockUser
    @Test
    void deleteById() throws Exception {
        this.mockMvc
                .perform(
                        delete("/v1/comment/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
