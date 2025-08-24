package co.com.crediya.api.rest.requests;

import co.com.crediya.api.config.PathsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class LoanRouterRest {

    private final PathsConfig pathsConfig;


    @Bean
    public RouterFunction<ServerResponse> routerFunction(LoanHandler handler) {
        return route(POST(pathsConfig.getLoans()), handler::listenSaveLoan);
    }
}
