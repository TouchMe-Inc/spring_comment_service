package by.touchme.commentservice.controller;

import by.touchme.commentservice.criteria.SearchCriteria;
import by.touchme.commentservice.criteria.SearchOperation;
import by.touchme.commentservice.dto.SearchDto;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SearchCommentController.class)
public class SearchCommentControllerUnitTest {


    final String URL = "/v1/comment/search";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService newsService;

    @MockBean
    PermissionService permissionService;

    @MockBean
    JwtFilter jwtFilter;

    @DisplayName("JUnit test for  SearchCommentController.search without body")
    @WithMockUser
    @Test
    void searchWithoutBody() throws Exception {
        mockMvc
                .perform(
                        get(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("JUnit test for  SearchCommentController.search without criteria list")
    @WithMockUser
    @Test
    void searchWithoutCriteriaList() throws Exception {
        SearchDto searchDto = new SearchDto();

        mockMvc
                .perform(
                        get(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(searchDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("JUnit test for  SearchCommentController.search")
    @WithMockUser
    @Test
    void search() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("text");
        searchCriteria.setOperation(SearchOperation.CONTAINS);
        searchCriteria.setValue("Lorem");

        SearchDto searchDto = new SearchDto();
        searchDto.setCriteriaList(List.of(searchCriteria));

        mockMvc
                .perform(
                        get(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(searchDto))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
