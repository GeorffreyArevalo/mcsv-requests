package co.com.crediya.api.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties( prefix = "routes.paths")
public class PathsConfig {

    private String loans;
    private String loansById;

}
