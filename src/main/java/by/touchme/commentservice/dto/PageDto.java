package by.touchme.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto<T> {
    List<T> content;
    Metadata metadata;

    public PageDto(Page<T> page) {
        this.content = page.getContent();
        this.metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Data
    @AllArgsConstructor
    static class Metadata {
        private long number;
        private long size;
        private long totalElements;
        private long totalPages;
    }
}
