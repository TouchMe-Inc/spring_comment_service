package by.touchme.commentservice.controller;

import by.touchme.commentservice.dto.CommentDto;
import by.touchme.commentservice.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class CommentControllerIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    final String URL = "/v1/comment";
    final String DOC_IDENTIFIER = "comment/{methodName}";

    final Long NOT_FOUND_ID = 99999L;
    final Long CORRECT_ID = 1L;
    final Long DELETE_ID = 2L;

    @MockBean
    PermissionService permissionService;

    @DisplayName("Integration test for CommentController.getPage")
    @Test
    void getPage() throws Exception {
        mockMvc.perform(
                        get(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(DOC_IDENTIFIER));
    }


    @DisplayName("Integration test for CommentController.getById")
    @Test
    void getById() throws Exception {
        mockMvc
                .perform(
                        get(URL + "/{id}", CORRECT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.getById with non existent id")
    @Test
    void getByIdWithNonExistentId() throws Exception {
        mockMvc
                .perform(
                        get(URL + "/{id}", NOT_FOUND_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.create")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void create() throws Exception {
        CommentDto createComment = new CommentDto();
        createComment.setNewsId(1L);
        createComment.setUsername("John Doe");
        createComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        mockMvc.perform(
                        post(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createComment))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.create with incorrect CommentDto")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void createWithIncorrectDto() throws Exception {
        CommentDto createComment = new CommentDto();

        mockMvc.perform(
                        post(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createComment))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.updateById")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void updateById() throws Exception {
        CommentDto updateComment = new CommentDto();
        updateComment.setNewsId(1L);
        updateComment.setUsername("John Doe");
        updateComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        mockMvc.perform(
                        put(URL + "/{id}", CORRECT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateComment))
                )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.updateById with incorrect CommentDto")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void updateByIdWithIncorrectDto() throws Exception {
        CommentDto updateComment = new CommentDto();

        mockMvc.perform(
                        put(URL + "/{id}", CORRECT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateComment))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.updateById with non existent id")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void updateByIdWithNonExistentId() throws Exception {
        CommentDto updateComment = new CommentDto();
        updateComment.setNewsId(1L);
        updateComment.setUsername("John Doe");
        updateComment.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        mockMvc.perform(
                        put(URL + "/{id}", NOT_FOUND_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateComment))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.deleteById")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void deleteById() throws Exception {
        mockMvc
                .perform(
                        delete(URL + "/{id}", DELETE_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(DOC_IDENTIFIER));
    }

    @DisplayName("Integration test for CommentController.deleteById with non existent id")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @Test
    void deleteByIdWithNonExistentId() throws Exception {
        mockMvc
                .perform(
                        delete(URL + "/{id}", NOT_FOUND_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document(DOC_IDENTIFIER));
    }
}
