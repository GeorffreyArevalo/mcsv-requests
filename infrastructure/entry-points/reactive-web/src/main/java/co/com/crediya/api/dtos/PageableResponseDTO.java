package co.com.crediya.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageableResponseDTO<T> {

    private LocalDateTime timestamp;
    private String message;
    private String code;
    private List<T> data;
    private List<String> errors;
    private Long total;
    private Integer  page;
    private Integer size;

}
