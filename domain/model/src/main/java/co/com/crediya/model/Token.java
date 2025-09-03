package co.com.crediya.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder( toBuilder = true )
public class Token {

    private String accessToken;
    private String role;
    private String subject;
    private List<String> permissions;

}
