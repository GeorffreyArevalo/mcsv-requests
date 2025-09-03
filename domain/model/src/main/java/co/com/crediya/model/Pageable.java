package co.com.crediya.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Pageable<T> {

    private Integer size;
    private Integer page;
    private Long total;
    private List<T> content;

}
