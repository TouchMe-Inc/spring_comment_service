package by.touchme.commentservice.dto;

import by.touchme.commentservice.criteria.SearchCriteria;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SearchDto {
    @NotNull
    private List<SearchCriteria> criteriaList;
}
