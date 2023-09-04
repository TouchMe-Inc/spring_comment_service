package by.touchme.commentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class NewsDto {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String text;

    private Date time;
}
